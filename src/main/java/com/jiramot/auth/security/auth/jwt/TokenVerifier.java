package com.jiramot.auth.security.auth.jwt;

public interface TokenVerifier {
  public boolean verify(String jti);
}
