package com.cyberpath.smartlearn.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TwoFactorDisableRequest {
    private String password; // para confirmar desactivación
    private String code; // código OTP actual
}

