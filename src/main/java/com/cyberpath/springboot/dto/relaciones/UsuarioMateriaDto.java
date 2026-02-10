package com.cyberpath.springboot.dto.relaciones;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioMateriaDto {
    private Integer id;

    private Integer idMateria;
    private Integer idUsuario;
}
