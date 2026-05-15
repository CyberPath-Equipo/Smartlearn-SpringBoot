package com.cyberpath.smartlearn.dto.ejercicio;

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
    private String tipo;
    private Integer orden;
    private Double puntos;

    private Integer idEjercicio;
}
