package com.cyberpath.smartlearn.configuracion.seguridad.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private long expiration;

    private SecretKey signingKey;

    @PostConstruct
    public void init() {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("jwt.secret no está configurada");
        }
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalStateException("jwt.secret demasiado corta: debe tener al menos 32 bytes (256 bits) para HS256");
        }
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        log.info("JwtService inicializado correctamente");
    }

    private Key getSigningKey() {
        return signingKey;
    }

    public String generarToken(String subject, Map<String, Object> extraClaims) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expiration);

        Map<String, Object> claims = new HashMap<>();
        if (extraClaims != null && !extraClaims.isEmpty()) {
            claims.putAll(extraClaims);
        }

        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(exp)
                .setId(UUID.randomUUID().toString())
                .signWith(getSigningKey(), SignatureAlgorithm.HS256);
        builder.addClaims(claims);

        return builder.compact();
    }

    public String generarToken(String subject) {
        return generarToken(subject, null);
    }

    public Claims obtenerClaims(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String obtenerSubject(String token) throws JwtException {
        return obtenerClaims(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            obtenerClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.debug("Token expirado: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException | MalformedJwtException | SecurityException | IllegalArgumentException e) {
            log.warn("Token JWT inválido: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.error("Error procesando JWT: {}", e.getMessage());
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            obtenerClaims(token);
            return false;
        } catch (ExpiredJwtException e) {
            log.debug("Token expirado: {}", e.getMessage());
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean isTokenValidForSubject(String token, String expectedSubject) {
        try {
            String subject = obtenerSubject(token);
            return subject != null && subject.equals(expectedSubject);
        } catch (JwtException e) {
            log.debug("Token inválido al validar subject: {}", e.getMessage());
            return false;
        }
    }
}