package com.cyberpath.smartlearn.dto.ejercicio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IntentoEjercicioDto {
    private Integer id;
    private Double puntaje;
    private Integer duracionSeg;
    private String fecha;
    private String estado;

    private Integer idUsuario;
    private Integer idEjercicio;
}
