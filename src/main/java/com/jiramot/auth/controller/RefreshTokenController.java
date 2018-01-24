package com.jiramot.auth.controller;

import com.jiramot.auth.security.TokenAuthenticationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class RefreshTokenController {
  @GetMapping("/refresh")
  public void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
    TokenAuthenticationService.refresh(request, response);
  }

}
