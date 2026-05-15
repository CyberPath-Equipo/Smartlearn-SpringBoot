package com.cyberpath.smartlearn.dto.recurso;

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

    private Integer idSubtema;
    private Integer idTipoRecurso;
}
