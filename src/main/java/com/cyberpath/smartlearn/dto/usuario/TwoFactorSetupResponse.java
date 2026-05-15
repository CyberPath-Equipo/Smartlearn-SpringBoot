package com.cyberpath.smartlearn.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TwoFactorSetupResponse {
    private String secret; // base32 secret para TOTP, null si SMS
    private String provisioningUri; // para generar QR
    private String transactionId; // para confirmar setup
    private List<String> recoveryCodes; // códigos de recuperación en texto plano (mostrar solo una vez)
}

