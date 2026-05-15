package com.cyberpath.smartlearn.controlador.usuario;

import com.cyberpath.smartlearn.configuracion.seguridad.login.LoginResponse;
import com.cyberpath.smartlearn.dto.usuario.*;
import com.cyberpath.smartlearn.modelo.usuario.Usuario;
import com.cyberpath.smartlearn.repositorio.usuario.UsuarioRepositorio;
import com.cyberpath.smartlearn.servicio.servicio.usuario.TwoFactorServicio;
import com.cyberpath.smartlearn.servicio.servicio.usuario.UsuarioServicio;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("/smartlearn/api/usuario/2fa")
@RestController
@CrossOrigin(origins = "*")
@AllArgsConstructor
@Slf4j
public class TwoFactorControlador {
    private final TwoFactorServicio twoFactorServicio;
    private final UsuarioServicio usuarioServicio;

    @PostMapping("/setup")
    public ResponseEntity<?> setup(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody TwoFactorSetupRequest request) {
        try {
            // Extraer ID del usuario autenticado del token
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Integer usuarioId = extractUsuarioIdFromAuth(auth);
            
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            TwoFactorSetupResponse response = twoFactorServicio.iniciateSetup(usuarioId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error en setup 2FA: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/confirm-setup")
    public ResponseEntity<?> confirmSetup(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody TwoFactorConfirmSetupRequest request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Integer usuarioId = extractUsuarioIdFromAuth(auth);
            
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            twoFactorServicio.confirmSetup(usuarioId, request);
            return ResponseEntity.ok().body(new MessageResponse("2FA activado exitosamente"));
        } catch (RuntimeException e) {
            log.error("Error en confirm setup 2FA: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody TwoFactorVerifyRequest request) {
        try {
            LoginResponse response = twoFactorServicio.verifyTwoFactor(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error en verify 2FA: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/disable")
    public ResponseEntity<?> disable(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody TwoFactorDisableRequest request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Integer usuarioId = extractUsuarioIdFromAuth(auth);
            
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            twoFactorServicio.disable2FA(usuarioId, request);
            return ResponseEntity.ok().body(new MessageResponse("2FA desactivado"));
        } catch (RuntimeException e) {
            log.error("Error en disable 2FA: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resend(@RequestBody TwoFactorResendRequest request) {
        try {
            twoFactorServicio.resendCode(request);
            return ResponseEntity.ok().body(new MessageResponse("Código reenviado"));
        } catch (RuntimeException e) {
            log.error("Error en resend 2FA: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/devices")
    public ResponseEntity<?> listDevices(
            @RequestHeader("Authorization") String authHeader) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Integer usuarioId = extractUsuarioIdFromAuth(auth);
            
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            List<Object> devices = twoFactorServicio.listTrustedDevices(usuarioId);
            return ResponseEntity.ok(devices);
        } catch (RuntimeException e) {
            log.error("Error listando dispositivos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/devices/{deviceId}")
    public ResponseEntity<?> revokeDevice(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long deviceId) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Integer usuarioId = extractUsuarioIdFromAuth(auth);
            
            if (usuarioId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            twoFactorServicio.revokeTrustedDevice(usuarioId, deviceId);
            return ResponseEntity.ok().body(new MessageResponse("Dispositivo revocado"));
        } catch (RuntimeException e) {
            log.error("Error revocando dispositivo: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    // Método auxiliar para extraer el ID del usuario del Authentication
    private Integer extractUsuarioIdFromAuth(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        // TODO: Implementar extracción del ID de usuario desde JWT
        // Opciones:
        // 1. Si el JWT contiene el ID: extraerlo del token
        // 2. Usar el nombre del principal (correo) para buscar el usuario en BD
        // Ejemplo:
        // String correo = auth.getName();
        // Usuario usuario = usuarioRepositorio.findByCorreo(correo);
        // return usuario != null ? usuario.getId() : null;

        String correo = auth.getName();
        Usuario usuario = usuarioServicio.findByCorreo(correo);
        return usuario != null ? usuario.getId() : null;
    }

    // DTOs internos para respuestas
    public static class ErrorResponse {
        public String error;
        public ErrorResponse(String error) {
            this.error = error;
        }
    }

    public static class MessageResponse {
        public String message;
        public MessageResponse(String message) {
            this.message = message;
        }
    }
}

