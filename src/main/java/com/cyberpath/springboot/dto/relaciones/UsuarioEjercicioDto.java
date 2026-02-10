package com.cyberpath.springboot.dto.relaciones;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEjercicioDto {
    private Integer id;

    private Integer idUsuario;
    private Integer idEjercicio;
    private boolean hecho;
}
