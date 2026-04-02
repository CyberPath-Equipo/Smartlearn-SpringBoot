package com.cyberpath.springboot.dto.ejercicio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EjercicioDto {
    private Integer id;
    private String nombre;
    private String tipo;
    private Integer dificultad;
    private Integer orden;
    private Boolean activo;

    private Integer idSubtema;
}
