package com.cyberpath.springboot.web;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordManager {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); //Encriptador de Spring Security

    // Encriptar contraseña
    public String encode(String contrasena) {
        return encoder.encode(contrasena);
    }

    // Validar contraseña ingresada vs contraseña en BD (encriptada)
    public boolean validarContrasena(String contrasenaIngresada, String contrasenaEncriptada) {
        return encoder.matches(contrasenaIngresada, contrasenaEncriptada);
    }
}
