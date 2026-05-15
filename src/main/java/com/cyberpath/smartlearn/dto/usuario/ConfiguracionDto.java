package com.cyberpath.smartlearn.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfiguracionDto {
    private Integer id;
    private boolean modoAudio;
    private boolean cuentaCreada;
    private boolean notificacionesActivadas;
    private String tamanoFuente;
    private boolean modoOffline;

    private Integer idUsuario;
}
