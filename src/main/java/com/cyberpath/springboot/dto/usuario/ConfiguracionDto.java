package com.cyberpath.springboot.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.cyberpath.springboot.modelo.usuario.Configuracion;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfiguracionDto {
    private Integer id;
    private boolean modoAudio;
    private boolean notificacionesActivadas;
    private Configuracion.TamanoFuente tamanoFuente;
    private boolean modoOffline;
    private boolean cuentaCreada;

    private Integer idUsuario;
}
