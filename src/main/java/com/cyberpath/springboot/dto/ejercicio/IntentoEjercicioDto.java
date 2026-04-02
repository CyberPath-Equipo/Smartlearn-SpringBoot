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
public class IntentoEjercicioDto {
    private Integer id;
    private BigDecimal puntaje;
    private Integer duracionSeg;
    private String fecha;
    private String estado;

    private Integer idUsuario;
    private Integer idEjercicio;
}
