package com.cyberpath.springboot.dto.ejercicio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PreguntaDto {
    private Integer id;
    private String enunciado;

    private Integer idEjercicio;
}
