package com.cyberpath.springboot.dto.recurso;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TipoRecursoDto {
    private Integer id;
    private String nombre;
    private String descripcion;
}
