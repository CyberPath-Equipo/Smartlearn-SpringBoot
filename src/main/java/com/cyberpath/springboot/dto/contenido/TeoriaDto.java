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
public class TeoriaDto {
    private Integer id;
    private String contenido;
    private boolean revisado;
    private String fuente;
    private LocalDateTime updatedAt;
    private Integer idSubtema;
}
