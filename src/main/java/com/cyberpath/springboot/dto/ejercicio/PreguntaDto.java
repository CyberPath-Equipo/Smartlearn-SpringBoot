package com.cyberpath.springboot.dto.ejercicio;

import com.cyberpath.springboot.modelo.ejercicio.TipoPregunta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PreguntaDto {
    private Integer id;
    private String enunciado;
    private TipoPregunta tipo;
    private Integer orden;
    private BigDecimal puntos;
    private Integer idEjercicio;
}
