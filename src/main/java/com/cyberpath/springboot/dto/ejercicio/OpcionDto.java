package com.cyberpath.springboot.dto.ejercicio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpcionDto {
    private Integer id;
    private String texto;
    private boolean correcta;

    private Integer idPregunta;
}
