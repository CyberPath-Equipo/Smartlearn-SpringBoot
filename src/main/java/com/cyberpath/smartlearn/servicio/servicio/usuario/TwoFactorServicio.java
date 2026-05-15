package com.cyberpath.smartlearn.servicio.servicio.usuario;

import com.cyberpath.smartlearn.configuracion.seguridad.login.LoginResponse;
import com.cyberpath.smartlearn.dto.usuario.*;

import java.util.List;

public interface TwoFactorServicio {
    
    // Setup: iniciar activación de 2FA
    TwoFactorSetupResponse iniciateSetup(Integer usuarioId, TwoFactorSetupRequest request);
    
    // Confirmar: confirmar OTP y activar 2FA
    void confirmSetup(Integer usuarioId, TwoFactorConfirmSetupRequest request);
    
    // Verify: verificar código 2FA en login
    LoginResponse verifyTwoFactor(TwoFactorVerifyRequest request);
    
    // Disable: desactivar 2FA
    void disable2FA(Integer usuarioId, TwoFactorDisableRequest request);
    
    // Resend: reenviar código de verificación por email
    void resendCode(TwoFactorResendRequest request);
    
    // Crear transacción 2FA (en login)
    String createTransaction(Integer usuarioId, String channel);

    // Registro: crear/verificar/reenviar código de confirmación de correo
    String createRegistrationTransaction(Integer usuarioId);
    void verifyRegistrationCode(String transactionId, String code);
    void resendRegistrationCode(String transactionId);
    
    // Validar dispositivo curado
    boolean validateTrustedDevice(String deviceToken);
    
    // Listar dispositivos de un usuario
    List<Object> listTrustedDevices(Integer usuarioId);
    
    // Revocar dispositivo
    void revokeTrustedDevice(Integer usuarioId, Long deviceId);
    
    // Validar y usar código de recuperación
    boolean validateAndUseRecoveryCode(Integer usuarioId, String code);
}

