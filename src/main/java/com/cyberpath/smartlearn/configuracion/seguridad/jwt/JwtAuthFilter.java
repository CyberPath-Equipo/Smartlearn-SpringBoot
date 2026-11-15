package com.cyberpath.smartlearn.configuracion.seguridad.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.toLowerCase().startsWith("bearer ")) {
            String token = authHeader.substring(authHeader.indexOf(' ') + 1).trim();

            try {
                if (jwtService.isTokenValid(token)) {
                    if (!autenticarUsuarioDesdeToken(token, request)) {
                        SecurityContextHolder.clearContext();
                        enviarJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Usuario no encontrado");
                        return;
                    }
                } else {
                    SecurityContextHolder.clearContext();
                    enviarJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o expirado");
                    return;
                }
            } catch (ExpiredJwtException ex) {
                log.debug("Access token expirado: {}", ex.getMessage());
                SecurityContextHolder.clearContext();
                enviarJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Token expirado");
                return;
            } catch (JwtException ex) {
                log.warn("Token JWT inválido: {}", ex.getMessage());
                SecurityContextHolder.clearContext();
                enviarJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getRequestURI();
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;
        return path.startsWith("/smartlearn/api/usuario/login") ||
                path.startsWith("/smartlearn/api/usuario/registro") ||
                path.startsWith("/smartlearn/api/usuario/login/docente") ||
                path.startsWith("/smartlearn/api/usuario/2fa/verify") ||
                path.startsWith("/smartlearn/api/usuario/2fa/resend");
    }

    private boolean autenticarUsuarioDesdeToken(String token, HttpServletRequest request) {
        String correo = jwtService.obtenerSubject(token);

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(correo);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.debug("Usuario autenticado: {}", correo);
            return true;
        } catch (UsernameNotFoundException ex) {
            return false;
        }
    }

    private void enviarJsonError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"error\":\"" + escapeJson(message) + "\"}");
        response.getWriter().flush();
    }

    private String escapeJson(String text) {
        return text == null ? "" : text.replace("\"", "\\\"");
    }
}