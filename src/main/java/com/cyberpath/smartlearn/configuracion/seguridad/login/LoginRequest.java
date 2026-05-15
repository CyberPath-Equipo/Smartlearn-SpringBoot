package com.cyberpath.smartlearn.configuracion.seguridad.login;

import lombok.Data;

@Data
public class LoginRequest {
    private String correo;
    private String contrasena;
}