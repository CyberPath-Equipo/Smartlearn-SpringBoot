package com.cyberpath.springboot.dto.usuario;

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

    private Integer idRol;
    private Integer idConfiguracion;
    private Integer idUltimaConexion;
}
