package com.cyberpath.smartlearn.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TwoFactorSetupRequest {
    private String password; // para confirmar la identidad
    private String method; // 'EMAIL' (canal actual), también admite legado
    private String phoneNumber; // opcional, legado si se reutiliza otro canal
}

