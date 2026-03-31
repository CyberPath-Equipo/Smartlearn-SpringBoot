package com.cyberpath.springboot.dto.ejercicio;

import com.cyberpath.springboot.modelo.ejercicio.EstadoIntentoEjercicio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IntentoEjercicioDto {
    private Integer id;
    private BigDecimal puntaje;
    private Integer duracionSeg;
    private LocalDateTime fecha;
    private EstadoIntentoEjercicio estado;

    private Integer idUsuario;
    private Integer idEjercicio;
}
