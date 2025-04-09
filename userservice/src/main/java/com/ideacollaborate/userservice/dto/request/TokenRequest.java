package com.ideacollaborate.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;
}
