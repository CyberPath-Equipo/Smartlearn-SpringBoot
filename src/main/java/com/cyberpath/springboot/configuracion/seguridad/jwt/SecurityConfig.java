package com.cyberpath.springboot.configuracion.seguridad.jwt;

import com.cyberpath.springboot.modelo.usuario.Usuario;
import com.cyberpath.springboot.servicio.servicio.usuario.UsuarioServicio;
import jakarta.servlet.Filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {

    private final JwtService jwtService;

    public SecurityConfig(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Bean
    public UserDetailsService userDetailsService(UsuarioServicio usuarioServicio) {
        return username -> {
            Usuario usuario = usuarioServicio.findByCorreo(username);
            if (usuario == null) throw new UsernameNotFoundException("No existe: " + username);

            return User.builder()
                    .username(usuario.getCorreo())
                    .password(usuario.getContrasena())
                    .authorities("ROLE_" + usuario.getRol().getTipo())
                    .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Registrar el filtro de bloqueo de navegador como bean (opcional) o instanciar en el método
    @Bean
    public BrowserBlockFilter browserBlockFilter() {
        return new BrowserBlockFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UsuarioServicio usuarioServicio) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/smartlearn/api/usuario/login",
                                "/smartlearn/api/usuario/login/docente",
                                "/smartlearn/api/usuario/registro").permitAll()

                        .requestMatchers(HttpMethod.GET, "/smartlearn/api/usuario").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/smartlearn/api/usuario", true)
                        .permitAll()
        )
                .addFilterBefore(browserBlockFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter(usuarioServicio), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter(UsuarioServicio usuarioServicio) {
        return new JwtAuthFilter(jwtService, usuarioServicio);
    }
}