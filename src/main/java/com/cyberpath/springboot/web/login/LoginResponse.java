package com.cyberpath.springboot.web.login;

import lombok.AllArgsConstructor;
import lombok.Data;

/// Clase para responder peticiones de login hechas para USUARIO
@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Integer idUsuario;
    private String nombreCuenta;
    private Integer idRol;
}
