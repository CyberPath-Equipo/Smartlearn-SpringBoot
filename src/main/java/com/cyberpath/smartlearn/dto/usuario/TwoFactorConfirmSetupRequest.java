package com.cyberpath.smartlearn.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TwoFactorConfirmSetupRequest {
    private String transactionId; // del setup
    private String code; // OTP de confirmación
    private String tempSecret; // secret temporal para confirmar
}

