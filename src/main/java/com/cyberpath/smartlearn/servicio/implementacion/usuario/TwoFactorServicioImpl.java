package com.cyberpath.smartlearn.servicio.implementacion.usuario;

import com.cyberpath.smartlearn.configuracion.seguridad.correo.CorreoServicio;
import com.cyberpath.smartlearn.configuracion.seguridad.jwt.JwtService;
import com.cyberpath.smartlearn.configuracion.seguridad.login.LoginResponse;
import com.cyberpath.smartlearn.dto.usuario.*;
import com.cyberpath.smartlearn.modelo.usuario.*;
import com.cyberpath.smartlearn.repositorio.usuario.*;
import com.cyberpath.smartlearn.servicio.servicio.usuario.TwoFactorServicio;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class TwoFactorServicioImpl implements TwoFactorServicio {

    private final UsuarioRepositorio usuarioRepositorio;
    private final TwoFactorTransactionRepositorio transactionRepositorio;
    private final TrustedDeviceRepositorio trustedDeviceRepositorio;
    private final RecoveryCodeRepositorio recoveryCodeRepositorio;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CorreoServicio correoServicio;

    // Constantes configurables
    private static final int TRANSACTION_EXPIRY_MINUTES = 5;
    private static final int TRUSTED_DEVICE_EXPIRY_DAYS = 30;
    private static final int RECOVERY_CODES_COUNT = 10;
    private static final int RECOVERY_CODE_LENGTH = 12;
    private static final String TOTP_ISSUER = "SmartLearn";
    private static final String EMAIL_CHANNEL = "EMAIL";

    @Override
    public TwoFactorSetupResponse iniciateSetup(Integer usuarioId, TwoFactorSetupRequest request) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getContrasena())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String method = request.getMethod() == null ? EMAIL_CHANNEL : request.getMethod().trim().toUpperCase(Locale.ROOT);
        if (EMAIL_CHANNEL.equals(method)) {
            return setupEmail(usuario);
        } else if ("TOTP".equals(method)) {
            return setupTOTP(usuario);
        } else if ("SMS".equals(method)) {
            return setupSMS(usuario, request.getPhoneNumber());
        }
        throw new RuntimeException("Método de 2FA no soportado. Usa EMAIL, TOTP o SMS.");
    }

    private TwoFactorSetupResponse setupEmail(Usuario usuario) {
        invalidateActiveTransaction(usuario.getId());

        if (usuario.getCorreo() == null || usuario.getCorreo().isBlank()) {
            throw new RuntimeException("El usuario no tiene un correo electrónico válido para 2FA");
        }

        String verificationCode = generateVerificationCode();
        String transactionId = generateTransactionId();

        TwoFactorTransaction transaction = TwoFactorTransaction.builder()
                .transactionId(transactionId)
                .usuario(usuario)
                .channel(EMAIL_CHANNEL)
                .verificationCodeHash(passwordEncoder.encode(verificationCode))
                .expiresAt(LocalDateTime.now().plusMinutes(TRANSACTION_EXPIRY_MINUTES))
                .used(false)
                .build();
        transactionRepositorio.save(transaction);

        correoServicio.enviarCodigoVerificacion(usuario.getCorreo(), usuario.getNombreCompleto(), verificationCode);

        return TwoFactorSetupResponse.builder()
                .secret(null)
                .provisioningUri(null)
                .transactionId(transactionId)
                .recoveryCodes(generateRecoveryCodes())
                .build();
    }

    private TwoFactorSetupResponse setupTOTP(Usuario usuario) {
        // Generar secret TOTP
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        String secret = key.getKey();

        // Generar URI de provisión para QR
        String accountName = usuario.getCorreo();
        String issuer = TOTP_ISSUER;
        String provisioningUri = String.format(
            "otpauth://totp/%s:%s?secret=%s&issuer=%s",
            URLEncoder.encode(issuer, StandardCharsets.UTF_8),
            URLEncoder.encode(accountName, StandardCharsets.UTF_8),
            secret,
            URLEncoder.encode(issuer, StandardCharsets.UTF_8)
        );

        String transactionId = generateTransactionId();

        TwoFactorTransaction transaction = TwoFactorTransaction.builder()
                .transactionId(transactionId)
                .usuario(usuario)
                .channel("TOTP")
                .expiresAt(LocalDateTime.now().plusMinutes(TRANSACTION_EXPIRY_MINUTES))
                .build();
        transactionRepositorio.save(transaction);

        return TwoFactorSetupResponse.builder()
                .secret(secret)
                .provisioningUri(provisioningUri)
                .transactionId(transactionId)
                .recoveryCodes(generateRecoveryCodes())
                .build();
    }

    private TwoFactorSetupResponse setupSMS(Usuario usuario, String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new RuntimeException("Número de teléfono requerido");
        }

        // Compatibilidad legada: se reutiliza el envío por email porque el canal activo es EMAIL.
        return setupEmail(usuario);
    }

    @Override
    public void confirmSetup(Integer usuarioId, TwoFactorConfirmSetupRequest request) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        TwoFactorTransaction transaction = transactionRepositorio.findByTransactionId(request.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Transacción inválida"));

        if (LocalDateTime.now().isAfter(transaction.getExpiresAt())) {
            throw new RuntimeException("Transacción expirada");
        }

        if (EMAIL_CHANNEL.equalsIgnoreCase(transaction.getChannel())) {
            if (transaction.getVerificationCodeHash() == null || !passwordEncoder.matches(request.getCode(), transaction.getVerificationCodeHash())) {
                throw new RuntimeException("Código invalido");
            }
            usuario.setTwoFactorSecret(null);
            usuario.setTwoFactorType(EMAIL_CHANNEL);
        } else {
            GoogleAuthenticator gAuth = new GoogleAuthenticator();
            if (!gAuth.authorize(request.getTempSecret(), Integer.parseInt(request.getCode()))) {
                throw new RuntimeException("Código invalido");
            }
            usuario.setTwoFactorSecret(encryptSecret(request.getTempSecret()));
            usuario.setTwoFactorType(transaction.getChannel());
        }

        usuario.setTwoFactorEnabled(true);
        usuarioRepositorio.save(usuario);

        transaction.setUsed(true);
        transactionRepositorio.save(transaction);

        List<String> recoveryCodes = generateRecoveryCodes();
        for (String code : recoveryCodes) {
            RecoveryCode rc = RecoveryCode.builder()
                    .usuario(usuario)
                    .codeHash(passwordEncoder.encode(code))
                    .used(false)
                    .build();
            recoveryCodeRepositorio.save(rc);
        }

        log.info("2FA confirmado y activado para usuario: {}", usuarioId);
    }

    @Override
    public LoginResponse verifyTwoFactor(TwoFactorVerifyRequest request) {
        TwoFactorTransaction transaction = transactionRepositorio.findByTransactionId(request.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Transacción inválida o expirada"));

        if (LocalDateTime.now().isAfter(transaction.getExpiresAt())) {
            throw new RuntimeException("Transacción expirada");
        }

        if (transaction.getUsed()) {
            throw new RuntimeException("Transacción ya usada");
        }

        Usuario usuario = transaction.getUsuario();
        String code = request.getCode().trim();

        if (EMAIL_CHANNEL.equalsIgnoreCase(transaction.getChannel())
                && transaction.getVerificationCodeHash() != null
                && passwordEncoder.matches(code, transaction.getVerificationCodeHash())) {
            return generateSuccessResponse(usuario, transaction, request);
        }

        if ("TOTP".equals(transaction.getChannel()) && usuario.getTwoFactorEnabled()) {
            GoogleAuthenticator gAuth = new GoogleAuthenticator();
            String decryptedSecret = decryptSecret(usuario.getTwoFactorSecret());
            if (gAuth.authorize(decryptedSecret, Integer.parseInt(code))) {
                return generateSuccessResponse(usuario, transaction, request);
            }
        }

        if (validateAndUseRecoveryCode(usuario.getId(), code)) {
            return generateSuccessResponse(usuario, transaction, request);
        }

        throw new RuntimeException("Código invalido");
    }

    private LoginResponse generateSuccessResponse(Usuario usuario, TwoFactorTransaction transaction, TwoFactorVerifyRequest request) {
        transaction.setUsed(true);
        transactionRepositorio.save(transaction);

        String token = jwtService.generarToken(usuario.getCorreo());

        String trustedDeviceToken = null;
        if (request.isRememberDevice()) {
            trustedDeviceToken = generateDeviceToken();
            TrustedDevice device = TrustedDevice.builder()
                    .usuario(usuario)
                    .deviceToken(trustedDeviceToken)
                    .deviceInfo(request.getDeviceInfo())
                    .expiresAt(LocalDateTime.now().plusDays(TRUSTED_DEVICE_EXPIRY_DAYS))
                    .revoked(false)
                    .build();
            trustedDeviceRepositorio.save(device);
            log.info("Dispositivo confiable creado para usuario: {}", usuario.getId());
        }

        log.info("2FA verificado exitosamente para usuario: {}", usuario.getId());

        return LoginResponse.builder()
                .token(token)
                .idUsuario(usuario.getId())
                .nombreCuenta(usuario.getNombreCuenta())
                .idRol(usuario.getRol().getId())
                .requires2fa(false)
                .trustedDeviceToken(trustedDeviceToken)
                .build();
    }

    @Override
    public void disable2FA(Integer usuarioId, TwoFactorDisableRequest request) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getContrasena())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        if (usuario.getTwoFactorEnabled()) {
            if (EMAIL_CHANNEL.equalsIgnoreCase(usuario.getTwoFactorType())) {
                if (!validateAndUseRecoveryCode(usuarioId, request.getCode())) {
                    throw new RuntimeException("Código invalido");
                }
            } else {
                GoogleAuthenticator gAuth = new GoogleAuthenticator();
                String decryptedSecret = decryptSecret(usuario.getTwoFactorSecret());
                if (!gAuth.authorize(decryptedSecret, Integer.parseInt(request.getCode()))) {
                    throw new RuntimeException("Código invalido");
                }
            }
        }

        usuario.setTwoFactorEnabled(false);
        usuario.setTwoFactorSecret(null);
        usuario.setTwoFactorType(null);
        usuarioRepositorio.save(usuario);

        List<TrustedDevice> devices = trustedDeviceRepositorio.findByUsuarioId(usuarioId);
        devices.forEach(d -> {
            d.setRevoked(true);
            trustedDeviceRepositorio.save(d);
        });

        List<RecoveryCode> codes = recoveryCodeRepositorio.findByUsuarioId(usuarioId);
        recoveryCodeRepositorio.deleteAll(codes);

        log.info("2FA desactivado para usuario: {}", usuarioId);
    }

    @Override
    public void resendCode(TwoFactorResendRequest request) {
        TwoFactorTransaction transaction = transactionRepositorio.findByTransactionId(request.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Transacción inválida"));

        if (!EMAIL_CHANNEL.equalsIgnoreCase(transaction.getChannel())) {
            throw new RuntimeException("Resend solo soportado para verificación por EMAIL");
        }

        String newCode = generateVerificationCode();
        transaction.setVerificationCodeHash(passwordEncoder.encode(newCode));
        transaction.setExpiresAt(LocalDateTime.now().plusMinutes(TRANSACTION_EXPIRY_MINUTES));
        transaction.setUsed(false);
        transactionRepositorio.save(transaction);

        Usuario usuario = transaction.getUsuario();
        correoServicio.enviarCodigoVerificacion(usuario.getCorreo(), usuario.getNombreCompleto(), newCode);

        log.info("Código EMAIL reenviado para transacción: {}", request.getTransactionId());
    }

    @Override
    public String createTransaction(Integer usuarioId, String channel) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getCorreo() == null || usuario.getCorreo().isBlank()) {
            throw new RuntimeException("El usuario no tiene un correo electrónico válido para 2FA");
        }

        invalidateActiveTransaction(usuarioId);

        String transactionId = generateTransactionId();
        String code = generateVerificationCode();

        TwoFactorTransaction transaction = TwoFactorTransaction.builder()
                .transactionId(transactionId)
                .usuario(usuario)
                .channel(EMAIL_CHANNEL)
                .verificationCodeHash(passwordEncoder.encode(code))
                .expiresAt(LocalDateTime.now().plusMinutes(TRANSACTION_EXPIRY_MINUTES))
                .used(false)
                .build();

        transactionRepositorio.save(transaction);
        correoServicio.enviarCodigoVerificacion(usuario.getCorreo(), usuario.getNombreCompleto(), code);
        return transactionId;
    }

    @Override
    public String createRegistrationTransaction(Integer usuarioId) {
        return createTransaction(usuarioId, EMAIL_CHANNEL);
    }

    @Override
    public void verifyRegistrationCode(String transactionId, String code) {
        TwoFactorTransaction transaction = transactionRepositorio.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transacción inválida o expirada"));

        if (LocalDateTime.now().isAfter(transaction.getExpiresAt())) {
            throw new RuntimeException("Transacción expirada");
        }

        if (Boolean.TRUE.equals(transaction.getUsed())) {
            throw new RuntimeException("Transacción ya usada");
        }

        if (!EMAIL_CHANNEL.equalsIgnoreCase(transaction.getChannel())) {
            throw new RuntimeException("Canal de verificación inválido");
        }

        if (transaction.getVerificationCodeHash() == null || !passwordEncoder.matches(code.trim(), transaction.getVerificationCodeHash())) {
            throw new RuntimeException("Código invalido");
        }

        Usuario usuario = transaction.getUsuario();
        usuario.setVerificado(true);
        usuario.setActivo(true);
        usuario.setActualizadoEn(LocalDateTime.now());
        usuarioRepositorio.save(usuario);

        transaction.setUsed(true);
        transactionRepositorio.save(transaction);
    }

    @Override
    public void resendRegistrationCode(String transactionId) {
        resendCode(TwoFactorResendRequest.builder().transactionId(transactionId).build());
    }

    @Override
    public boolean validateTrustedDevice(String deviceToken) {
        Optional<TrustedDevice> device = trustedDeviceRepositorio.findByDeviceToken(deviceToken);

        if (device.isEmpty()) {
            return false;
        }

        TrustedDevice td = device.get();

        if (td.getRevoked()) {
            return false;
        }

        if (td.getExpiresAt() != null && LocalDateTime.now().isAfter(td.getExpiresAt())) {
            td.setRevoked(true);
            trustedDeviceRepositorio.save(td);
            return false;
        }

        return true;
    }

    @Override
    public List<Object> listTrustedDevices(Integer usuarioId) {
        List<TrustedDevice> devices = trustedDeviceRepositorio.findByUsuarioIdAndRevokedFalse(usuarioId);
        return devices.stream().map(d -> new Object() {
            public Long id = d.getId();
            public String deviceInfo = d.getDeviceInfo();
            public LocalDateTime createdAt = d.getCreatedAt();
            public LocalDateTime expiresAt = d.getExpiresAt();
        }).collect(Collectors.toList());
    }

    @Override
    public void revokeTrustedDevice(Integer usuarioId, Long deviceId) {
        TrustedDevice device = trustedDeviceRepositorio.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Dispositivo no encontrado"));

        if (!device.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No autorizado");
        }

        device.setRevoked(true);
        trustedDeviceRepositorio.save(device);
        log.info("Dispositivo revocado: {}", deviceId);
    }

    @Override
    public boolean validateAndUseRecoveryCode(Integer usuarioId, String code) {
        List<RecoveryCode> codes = recoveryCodeRepositorio.findByUsuarioIdAndUsedFalse(usuarioId);

        for (RecoveryCode rc : codes) {
            if (passwordEncoder.matches(code, rc.getCodeHash())) {
                rc.setUsed(true);
                recoveryCodeRepositorio.save(rc);
                log.info("Recovery code usado para usuario: {}", usuarioId);
                return true;
            }
        }

        return false;
    }

    // Métodos auxiliares

    private String generateTransactionId() {
        return "tx_" + UUID.randomUUID().toString().replace("-", "").substring(0, 20);
    }

    private String generateDeviceToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String generateVerificationCode() {
        return String.format("%06d", new SecureRandom().nextInt(1_000_000));
    }

    private List<String> generateRecoveryCodes() {
        List<String> codes = new ArrayList<>();
        SecureRandom random = new SecureRandom();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        for (int i = 0; i < RECOVERY_CODES_COUNT; i++) {
            StringBuilder code = new StringBuilder();
            for (int j = 0; j < RECOVERY_CODE_LENGTH; j++) {
                code.append(chars.charAt(random.nextInt(chars.length())));
            }
            codes.add(code.toString());
        }

        return codes;
    }

    // Encriptación simple (en producción usar KMS o similar)
    private String encryptSecret(String secret) {
        return secret; // TODO: implementar cifrado real
    }

    private String decryptSecret(String encryptedSecret) {
        return encryptedSecret; // TODO: implementar descifrado real
    }

    private void invalidateActiveTransaction(Integer usuarioId) {
        transactionRepositorio.findByUsuarioIdAndUsedFalse(usuarioId).ifPresent(active -> {
            active.setUsed(true);
            transactionRepositorio.save(active);
        });
    }

}
