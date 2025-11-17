package com.cyberpath.springboot.dto.contenido;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeoriaDto {
    private Integer id;
    private String contenido;
    private boolean revisado;

    private Integer idSubtema;
}
