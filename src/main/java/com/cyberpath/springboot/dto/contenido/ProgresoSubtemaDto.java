package com.cyberpath.springboot.dto.contenido;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgresoSubtemaDto {
    private Integer id;
    private boolean teoriaLeida;
    private Integer ejerciciosCompletados;
    private Integer ejerciciosTotales;
    private double porcentaje;
    private LocalDateTime ultimoAcceso;

    private Integer idUsuario;
    private Integer idSubtema;
}
