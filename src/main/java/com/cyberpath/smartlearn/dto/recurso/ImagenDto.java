package com.cyberpath.smartlearn.dto.recurso;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImagenDto {
    private Long id;
    private String nombre;
    private String imagenUrl;
}
