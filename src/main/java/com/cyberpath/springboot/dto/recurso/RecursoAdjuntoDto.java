package com.cyberpath.springboot.dto.recurso;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecursoAdjuntoDto {
    private Integer id;
    private Integer orden;
    private String titulo;
    private String url;
    private String mimeType;
    private Long tamanoBytes;
    private String descripcion;
    private LocalDateTime creadoEn;

    private Integer idSubtema;
    private Integer idTipoRecurso;
}
