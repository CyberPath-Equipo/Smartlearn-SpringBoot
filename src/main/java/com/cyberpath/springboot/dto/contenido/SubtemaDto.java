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
public class SubtemaDto {
    private Integer id;
    private String nombre;
    private Integer orden;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer idTema;
}
