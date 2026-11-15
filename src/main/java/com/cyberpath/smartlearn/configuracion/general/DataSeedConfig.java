package com.cyberpath.smartlearn.configuracion.general;

import com.cyberpath.smartlearn.dto.usuario.UsuarioDto;
import com.cyberpath.smartlearn.modelo.usuario.Usuario;
import com.cyberpath.smartlearn.repositorio.usuario.RolRepositorio;
import com.cyberpath.smartlearn.servicio.servicio.usuario.UsuarioServicio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class DataSeedConfig {
    @Value("${smartlearn.seed-admin.username}")
    private String adminUsername;
    @Value("${smartlearn.seed-admin.email}")
    private String adminEmail;
    @Value("${smartlearn.seed-admin.password}")
    private String adminPassword;

    @Bean
    CommandLineRunner initDatabase(UsuarioServicio usuarioServicio, RolRepositorio rolRepository) {
        return args -> {
            Usuario adminExistente = usuarioServicio.findByNombreCuenta("admin");

            if (adminExistente == null) {
                log.info("Usuario 'admin' no encontrado. Procediendo a la creación");

                UsuarioDto adminDto = UsuarioDto.builder()
                        .nombreCuenta(adminUsername)
                        .nombreCompleto(adminUsername)
                        .correo(adminEmail)
                        .contrasena(adminPassword)
                        .idRol(3)
                        .verificado(true)
                        .build();

                usuarioServicio.save(adminDto);

                log.info("Administrador creado exitosamente en el sistema.");
            } else {
                log.info("El administrador existe correctamente en el sistema.");
            }
        };
    }
}