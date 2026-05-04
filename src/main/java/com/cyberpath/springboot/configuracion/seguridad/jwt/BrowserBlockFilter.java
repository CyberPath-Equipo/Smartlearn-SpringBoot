package com.cyberpath.springboot.configuracion.seguridad.jwt;

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
        String ua = request.getHeader("User-Agent");
        if (ua == null) return false;
        ua = ua.toLowerCase(Locale.ROOT);
        // patrones básicos para detectar navegadores; puedes ampliarlos si lo deseas
        return ua.contains("mozilla") || ua.contains("chrome") || ua.contains("safari")
                || ua.contains("firefox") || ua.contains("edge") || ua.contains("opera");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

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
                    "  <title>Página protegida</title>\n" +
                    "  <style>\n" +
                    "    body { font-family: Arial, Helvetica, sans-serif; background:#f8f9fa; color:#212529; display:flex; align-items:center; justify-content:center; height:100vh; margin:0 }\n" +
                    "    .card { background:#fff; border-radius:8px; box-shadow:0 2px 10px rgba(0,0,0,0.08); padding:24px; max-width:520px; text-align:center }\n" +
                    "    h1 { margin:0 0 8px; font-size:1.4rem }\n" +
                    "    p { margin:0 0 16px; color:#495057 }\n" +
                    "    a.button { display:inline-block; padding:10px 16px; background:#0d6efd; color:#fff; text-decoration:none; border-radius:6px }\n" +
                    "    a.button:hover { background:#0b5ed7 }\n" +
                    "    .small { margin-top:8px; color:#6c757d; font-size:0.9rem }\n" +
                    "  </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "  <div class=\"card\">\n" +
                    "    <h1>Acceso restringido</h1>\n" +
                    "    <p>Esta página está protegida.</p>\n" +
                    "    <a class=\"button\" href=\"/login\">Iniciar sesión</a>\n" +
                    "    <div class=\"small\">Si crees que deberías tener acceso, inicia sesión con una cuenta con los permisos adecuados.</div>\n" +
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