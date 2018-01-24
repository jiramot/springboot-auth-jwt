package com.jiramot.auth.security;

import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JWTAuthenticationFailureHandler {
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

  }
}
