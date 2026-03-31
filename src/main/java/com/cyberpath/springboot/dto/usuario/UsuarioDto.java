package com.cyberpath.springboot.dto.usuario;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDto {
    private Integer id;
    private String nombreCuenta;
    private String correo;
    private String contrasena;
    private String nombreCompleto;
    private Boolean activo;
    private Boolean verificado;
    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;

    private Integer idRol;
}
