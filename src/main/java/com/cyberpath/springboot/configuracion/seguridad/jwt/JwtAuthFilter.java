package com.cyberpath.springboot.configuracion.seguridad.jwt;

import com.cyberpath.springboot.modelo.usuario.Usuario;
import com.cyberpath.springboot.servicio.servicio.usuario.UsuarioServicio;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import io.jsonwebtoken.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioServicio usuarioServicio;

    public JwtAuthFilter(JwtService jwtService, UsuarioServicio usuarioServicio) {
        this.jwtService = jwtService;
        this.usuarioServicio = usuarioServicio;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.toLowerCase().startsWith("bearer ")) {
            String token = authHeader.substring(authHeader.indexOf(' ') + 1).trim();

            try {
                if (!jwtService.isTokenValid(token)) {
                    SecurityContextHolder.clearContext();
                    sendJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o expirado");
                    return;
                }

                String correo = jwtService.obtenerSubject(token);
                if (correo != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    Usuario usuario = usuarioServicio.findByCorreo(correo);
                    if (usuario != null) {
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
                        log.debug("Usuario autenticado: {}", correo);
                    } else {
                        SecurityContextHolder.clearContext();
                        sendJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Usuario no encontrado");
                        return;
                    }
                }
            } catch (JwtException ex) {
                log.warn("Token JWT inválido: {}", ex.getMessage());
                SecurityContextHolder.clearContext();
                sendJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
                return;
            } catch (Exception ex) {
                log.error("Error procesando token JWT", ex);
                SecurityContextHolder.clearContext();
                sendJsonError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;
        return path.startsWith("/smartlearn/api/usuario/login") ||
                path.startsWith("/smartlearn/api/usuario/registro") ||
                path.startsWith("/smartlearn/api/usuario/login/docente");
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