package com.cyberpath.springboot.web.login;

import lombok.Data;

/// Clase para manejar peticiones de Login hechas desde la web
@Data
public class LoginRequest {
    private String correo; // Correo ingresado por el usuario a la hora de hacer la petición para login
    private String contrasena; // Contraseña ingresada por el usuario
}