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
    private Integer idUsuario;
    private String nombreCuenta;
    private Integer idRol;
    
    private Boolean requires2fa;
    private String twoFactorTransactionId;
    private String twoFactorChannel;
    
    private String trustedDeviceToken;
}
