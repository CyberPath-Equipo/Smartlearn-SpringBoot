package com.cyberpath.smartlearn.configuracion.seguridad.jwt;

import com.cyberpath.smartlearn.modelo.usuario.Usuario;
import com.cyberpath.smartlearn.servicio.servicio.usuario.UsuarioServicio;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String REFRESH_TOKEN_HEADER = "X-Refresh-Token";
    private static final String NEW_ACCESS_TOKEN_HEADER = "X-New-Access-Token";

    private final JwtService jwtService;
    private final UsuarioServicio usuarioServicio;

    public JwtAuthFilter(JwtService jwtService, UsuarioServicio usuarioServicio) {
        this.jwtService = jwtService;
        this.usuarioServicio = usuarioServicio;
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
                if (jwtService.isAccessTokenValid(token)) {
                    if (!autenticarUsuarioDesdeToken(token, request)) {
                        SecurityContextHolder.clearContext();
                        sendJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Usuario no encontrado");
                        return;
                    }
                } else if (jwtService.isTokenExpired(token)) {
                    String refreshToken = request.getHeader(REFRESH_TOKEN_HEADER);
                    if (refreshToken != null && jwtService.isRefreshTokenValid(refreshToken)) {
                        String correo = jwtService.obtenerSubject(refreshToken);
                        Usuario usuario = usuarioServicio.findByCorreo(correo);
                        if (usuario != null) {
                            if (autenticarUsuario(request, usuario)) {
                                response.setHeader(NEW_ACCESS_TOKEN_HEADER, jwtService.generarToken(correo));
                                log.debug("Access token renovado automáticamente para: {}", correo);
                            }
                        } else {
                            SecurityContextHolder.clearContext();
                            sendJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Usuario no encontrado");
                            return;
                        }
                    } else {
                        SecurityContextHolder.clearContext();
                        sendJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Token expirado y refresh token ausente o inválido");
                        return;
                    }
                } else {
                    SecurityContextHolder.clearContext();
                    sendJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o expirado");
                    return;
                }
            } catch (ExpiredJwtException ex) {
                log.debug("Access token expirado: {}", ex.getMessage());
                SecurityContextHolder.clearContext();
                sendJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Token expirado");
                return;
            } catch (JwtException ex) {
                log.warn("Token JWT inválido: {}", ex.getMessage());
                SecurityContextHolder.clearContext();
                sendJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
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
                path.startsWith("/smartlearn/api/usuario/token/refresh") ||
                path.startsWith("/smartlearn/api/usuario/registro") ||
                path.startsWith("/smartlearn/api/usuario/login/docente") ||
                path.startsWith("/smartlearn/api/usuario/2fa/verify") ||
                path.startsWith("/smartlearn/api/usuario/2fa/resend");
    }

    private boolean autenticarUsuarioDesdeToken(String token, HttpServletRequest request) {
        String correo = jwtService.obtenerSubject(token);
        Usuario usuario = usuarioServicio.findByCorreo(correo);
        if (usuario == null) {
            return false;
        }

        return autenticarUsuario(request, usuario);
    }

    private boolean autenticarUsuario(HttpServletRequest request, Usuario usuario) {
        List<SimpleGrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getTipo()));

        UserDetails userDetails = User.builder()
                .username(usuario.getCorreo())
                .password(usuario.getContrasena() == null ? "" : usuario.getContrasena())
                .authorities(authorities)
                .build();

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
        log.debug("Usuario autenticado: {}", usuario.getCorreo());
        return true;
    }

    private void sendJsonError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        // Respuesta JSON simple y consistente para el cliente
        response.getWriter().write("{\"error\":\"" + escapeJson(message) + "\"}");
        response.getWriter().flush();
    }

    private String escapeJson(String text) {
        return text == null ? "" : text.replace("\"", "\\\"");
    }
}