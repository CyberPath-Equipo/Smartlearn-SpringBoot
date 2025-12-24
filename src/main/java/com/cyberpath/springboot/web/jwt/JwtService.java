package com.cyberpath.springboot.web.jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); //Llave de seguridad Web

    public String generarToken(String subject) { //Token para manejo de usuarios y transacciones web| Subject es un identificador, en este caso el correo
        return Jwts.builder() // Nuevo JWT (JSON Web Token
                .setSubject(subject) // Asigna el correo como identificador
                .setIssuedAt(new Date()) // Fecha exacta de creación del Token
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // Fecha exacta de expiración del Token (Fecha de creación + 1 día)
                .signWith(secretKey) // Se ingresa al token mediante una llave secreta
                .compact();
    }

    public String obtenerSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
