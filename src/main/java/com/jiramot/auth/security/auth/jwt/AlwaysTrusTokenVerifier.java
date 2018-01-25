package com.jiramot.auth.security.auth.jwt;

import org.springframework.stereotype.Component;

@Component
public class AlwaysTrusTokenVerifier implements TokenVerifier {
  @Override
  public boolean verify(String jti) {
    return true;
  }
}