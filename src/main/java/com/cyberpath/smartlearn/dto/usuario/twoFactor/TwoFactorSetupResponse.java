package com.cyberpath.smartlearn.dto.usuario.twoFactor;

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
    private String secret;
    private String provisioningUri;
    private String transactionId;
    private List<String> recoveryCodes;
}

