package com.jiramot.auth.security.auth.username;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiramot.auth.security.LoginRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class UsernameAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private final AuthenticationSuccessHandler successHandler;
  private final AuthenticationFailureHandler failureHandler;
  private final ObjectMapper objectMapper;

  public UsernameAuthenticationFilter(String defaultProcessUrl, AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler, ObjectMapper mapper) {
    super(defaultProcessUrl);
    this.successHandler = successHandler;
    this.failureHandler = failureHandler;
    this.objectMapper = mapper;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
      throws AuthenticationException, IOException, ServletException {

    LoginRequest loginRequest = objectMapper.readValue(req.getInputStream(), LoginRequest.class);

    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
        loginRequest.getUsername(),
        loginRequest.getPassword(),
        Collections.emptyList()
    );

    return getAuthenticationManager().authenticate(token);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth)
      throws IOException, ServletException {
    successHandler.onAuthenticationSuccess(req, res, auth);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            AuthenticationException failed) throws IOException, ServletException {
    SecurityContextHolder.clearContext();
    failureHandler.onAuthenticationFailure(request, response, failed);
  }
}