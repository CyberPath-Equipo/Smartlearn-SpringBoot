package com.cyberpath.smartlearn.controlador.usuario.contrasena;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CambioPasswordDto {
    private String passwordActual;
    private String passwordNueva;
}
