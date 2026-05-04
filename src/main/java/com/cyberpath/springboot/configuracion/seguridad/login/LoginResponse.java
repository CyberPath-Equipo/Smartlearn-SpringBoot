package com.cyberpath.springboot.configuracion.seguridad.login;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Integer idUsuario;
    private String nombreCuenta;
    private Integer idRol;
}
