package com.cyberpath.smartlearn.dto.usuario.twoFactor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TwoFactorSetupRequest {
    private String password;
    private String method;
    private String phoneNumber;
}

