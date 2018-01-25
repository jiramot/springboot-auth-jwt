package com.jiramot.auth.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "auth.jwt")
@Data
public class JwtSettings {
  private Integer tokenExpirationTime;
  private String tokenIssuer;
  private String tokenSigningKey;
  private Integer refreshTokenExpTime;
}
