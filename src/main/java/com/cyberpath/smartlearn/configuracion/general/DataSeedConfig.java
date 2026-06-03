package com.cyberpath.smartlearn.configuracion.general;

import com.cyberpath.smartlearn.dto.usuario.UsuarioDto;
import com.cyberpath.smartlearn.modelo.usuario.Usuario;
import com.cyberpath.smartlearn.repositorio.usuario.RolRepositorio;
import com.cyberpath.smartlearn.servicio.servicio.usuario.UsuarioServicio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class DataSeedConfig {

    @Bean
    CommandLineRunner initDatabase(UsuarioServicio usuarioServicio, RolRepositorio rolRepository) {
        return args -> {
            log.info("Verificando existencia del administrador inicial...");

            Usuario adminExistente = usuarioServicio.findByNombreCuenta("admin");

            if (adminExistente == null) {
                log.info("Usuario 'admin' no encontrado. Procediendo a la creación...");

                UsuarioDto adminDto = UsuarioDto.builder()
                        .nombreCuenta("admin")
                        .nombreCompleto("Administrador del Sistema")
                        .correo("admin@smartlearn.com")
                        .contrasena("123")
                        .idRol(3)
                        .verificado(true)
                        .build();

                usuarioServicio.save(adminDto);

                log.info("¡Usuario 'admin' creado exitosamente a través del servicio!");
            } else {
                log.info("El administrador existe correctamente en el sistema.");
            }
        };
    }
}