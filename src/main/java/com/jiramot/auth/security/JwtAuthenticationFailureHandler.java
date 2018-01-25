package com.jiramot.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {
  private final ObjectMapper mapper;

  @Autowired
  public JwtAuthenticationFailureHandler(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                      AuthenticationException e) throws IOException, ServletException {

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);


    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("error", "UNAUTHORIZED");
    mapper.writeValue(response.getWriter(), errorResponse);
  }
}
