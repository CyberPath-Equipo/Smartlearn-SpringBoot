package com.cyberpath.springboot.dto.contenido;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TemaDto {
    private Integer id;
    private String nombre;

    private Integer idMateria;
}
