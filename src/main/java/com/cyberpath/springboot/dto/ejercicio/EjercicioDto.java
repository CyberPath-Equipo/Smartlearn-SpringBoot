package com.cyberpath.springboot.dto.ejercicio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EjercicioDto {
    private Integer id;
    private String nombre;
    private boolean hecho;

    private Integer idSubtema;
}
