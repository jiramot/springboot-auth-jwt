package com.jiramot.auth.security.model.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jsonwebtoken.Claims;
import lombok.Getter;


public final class AccessJwtToken implements JwtToken {
  private final String rawToken;
  @Getter
  @JsonIgnore
  private Claims claims;

  public AccessJwtToken(String rawToken, Claims claims) {
    this.rawToken = rawToken;
    this.claims = claims;
  }

  @Override
  public String getToken() {
    return rawToken;
  }
}
