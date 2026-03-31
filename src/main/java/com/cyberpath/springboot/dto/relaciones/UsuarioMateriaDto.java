package com.cyberpath.springboot.dto.relaciones;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioMateriaDto {
    private Integer idMateria;
    private Integer idUsuario;
    private LocalDateTime suscritoEn;
}
