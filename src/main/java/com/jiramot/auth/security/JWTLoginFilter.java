package com.jiramot.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {
  private static Logger logger = LoggerFactory.getLogger(JWTLoginFilter.class);

  private final JWTAuthenticationSuccessHandler successHandler;
  private final JWTAuthenticationFailureHandler failureHandler;

  private final ObjectMapper objectMapper;

  public JWTLoginFilter(String defaultProcessUrl, JWTAuthenticationSuccessHandler successHandler,
                        JWTAuthenticationFailureHandler failureHandler, ObjectMapper mapper) {
    super(defaultProcessUrl);
    this.successHandler = successHandler;
    this.failureHandler = failureHandler;
    this.objectMapper = mapper;
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest req, HttpServletResponse res)
      throws AuthenticationException, IOException, ServletException {

    LoginRequest loginRequest = new ObjectMapper().readValue(req.getInputStream(), LoginRequest.class);
    if (StringUtils.isBlank(loginRequest.getUsername()) || StringUtils.isBlank(loginRequest.getPassword())) {
      throw new AuthenticationServiceException("Username or Password not provided");
    }
    AuthenticationManager authenticationManager = getAuthenticationManager();
    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword(), Collections.emptyList()
    );

    return authenticationManager.authenticate(token);
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest req,
      HttpServletResponse res, FilterChain chain,
      Authentication auth) throws IOException, ServletException {

    successHandler.onAuthenticationSuccess(req, res, auth);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            AuthenticationException failed) throws IOException, ServletException {
    SecurityContextHolder.clearContext();
    failureHandler.onAuthenticationFailure(request, response, failed);
  }
}