package com.ideacollaborate.userservice.security;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {
    private boolean enabled;
    private String secret;
    private long accessExpiration;
    private long refreshExpiration;
}
