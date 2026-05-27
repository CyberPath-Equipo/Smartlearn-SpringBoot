package com.cyberpath.smartlearn.configuracion.seguridad.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private String refreshToken;
    private Integer idUsuario;
    private String nombreCuenta;
    private Integer idRol;
    
    // Campos para 2FA
    private Boolean requires2fa;
    private String twoFactorTransactionId;
    private String twoFactorChannel;
    
    // Para respuesta de verificación 2FA
    private String trustedDeviceToken;
}
