package com.cyberpath.springboot.dto.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String contrasena;
    private String nombreCompleto;
    private Boolean activo;
    private Boolean verificado;
    private Integer idRol;
    private Integer idConfiguracion;
    private Integer idUltimaConexion;
}
