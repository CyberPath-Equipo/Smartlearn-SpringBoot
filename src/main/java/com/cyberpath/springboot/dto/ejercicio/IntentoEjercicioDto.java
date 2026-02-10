package com.cyberpath.springboot.dto.ejercicio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IntentoEjercicioDto {
    private Integer id;
    private double puntaje;
    private String fecha;

    private Integer idUsuario;
    private Integer idEjercicio;
}
