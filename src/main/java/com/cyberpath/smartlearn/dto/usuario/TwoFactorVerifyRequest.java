package com.cyberpath.smartlearn.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TwoFactorVerifyRequest {
    private String transactionId;
    private String code;
    private boolean rememberDevice;
    private String deviceInfo;
}

