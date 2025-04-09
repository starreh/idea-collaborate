package com.ideacollaborate.userservice.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {

    @NotBlank
    private String userId;

    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;
}
