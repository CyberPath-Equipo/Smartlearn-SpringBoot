package com.cyberpath.springboot.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UltimaConexionDto {
    private Integer id;
    private LocalDateTime ultimaConexion;
    private String dispositivo;

    private Integer idUsuario;
    private Integer idSubtema;
}
