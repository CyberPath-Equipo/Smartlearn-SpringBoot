package com.cyberpath.smartlearn.configuracion.seguridad.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

public class BrowserBlockFilter extends OncePerRequestFilter {

    // Ruta que permitimos ver desde el navegador
    private static final String ALLOWED_BROWSER_GET = "/smartlearn/api/usuario";

    private boolean isBrowserUserAgent(HttpServletRequest request) {
        // si viene de la app movil explícita, no considerar navegador
        String clientType = request.getHeader("X-Client-Type");
        if ("mobile".equalsIgnoreCase(clientType)) return false;

        String ua = request.getHeader("User-Agent");
        if (ua == null) return false;
        ua = ua.toLowerCase(Locale.ROOT);
        return ua.contains("mozilla") || ua.contains("chrome") || ua.contains("safari")
                || ua.contains("firefox") || ua.contains("edge") || ua.contains("opera");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.toLowerCase().startsWith("bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getRequestURI();
        // Normalizar para quitar parámetros de query si los hay
        int q = path.indexOf('?');
        if (q != -1) path = path.substring(0, q);

        String method = request.getMethod();

        // Solo nos interesa bloquear GETs bajo /smartlearn/api/
        boolean isAllowedUsuarioPath = path.equals(ALLOWED_BROWSER_GET) || path.equals(ALLOWED_BROWSER_GET + "/");
        if ("GET".equalsIgnoreCase(method)
                && path.startsWith("/smartlearn/api/")
                && !isAllowedUsuarioPath
                && isBrowserUserAgent(request)) {

            // Devolver HTML personalizado informando que es una página protegida
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("text/html;charset=UTF-8");

            String html = "<!doctype html>\n" +
                    "<html lang=\"es\">\n" +
                    "<head>\n" +
                    "  <meta charset=\"utf-8\">\n" +
                    "  <meta name=\"viewport\" content=\"width=device-width,initial-scale=1\">\n" +
                    "  <title>Acceso Restringido</title>\n" +
                    "  <style>\n" +
                    "    body { font-family: Arial, Helvetica, sans-serif; background:#f8f9fa; color:#212529; display:flex; align-items:center; justify-content:center; height:100vh; margin:0 }\n" +
                    "    .card { background:#fff; border-radius:12px; box-shadow:0 4px 20px rgba(0,0,0,0.1); padding:40px 30px; max-width:480px; text-align:center }\n" +
                    "    .icon { font-size:3.5rem; margin-bottom:16px }\n" +
                    "    h1 { margin:0 0 12px; font-size:1.6rem; color:#2c3e50 }\n" +
                    "    p { margin:0 0 24px; color:#495057; line-height:1.5 }\n" +
                    "    a.button { display:inline-block; padding:12px 24px; background:#0d6efd; color:#fff; text-decoration:none; border-radius:8px; font-weight:500; margin:8px }\n" +
                    "    a.button:hover { background:#0b5ed7 }\n" +
                    "    a.button-secondary { background:#6c757d }\n" +
                    "    a.button-secondary:hover { background:#5a6268 }\n" +
                    "    .small { margin-top:20px; color:#6c757d; font-size:0.9rem }\n" +
                    "  </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "  <div class=\"card\">\n" +
                    "    <div class=\"icon\">🔒</div>\n" +
                    "    <h1>Acceso Restringido</h1>\n" +
                    "    <p>No tienes permiso para acceder a esta página o tu sesión ha expirado.</p>\n" +
                    "    \n" +
                    "    <a class=\"button\" href=\"javascript:history.back()\">← Regresar</a>\n" +
                    "    \n" +
                    "    <div class=\"small\">Si crees que deberías tener acceso, inicia sesión con una cuenta autorizada.</div>\n" +
                    "  </div>\n" +
                    "</body>\n" +
                    "</html>";

            try (PrintWriter out = response.getWriter()) {
                out.write(html);
                out.flush();
            }
            return;
        }

        filterChain.doFilter(request, response);
    }
}