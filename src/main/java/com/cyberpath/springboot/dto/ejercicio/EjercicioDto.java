package com.cyberpath.springboot.dto.ejercicio;

import com.cyberpath.springboot.modelo.ejercicio.TipoEjercicio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EjercicioDto {
    private Integer id;
    private String nombre;
    private TipoEjercicio tipo;
    private Integer dificultad;
    private Integer orden;
    private Boolean activo;
    private LocalDateTime createdAt;
    private Integer idSubtema;
}
