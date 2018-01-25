package com.jiramot.auth.security;

import com.jiramot.auth.user.User;
import com.jiramot.auth.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

  private UserService userService;
  private BCryptPasswordEncoder encoder;

  @Autowired
  public JwtAuthenticationProvider(UserService userService, BCryptPasswordEncoder encoder) {
    this.userService = userService;
    this.encoder = encoder;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = (String) authentication.getPrincipal();
    String password = (String) authentication.getCredentials();

    User user = userService.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    if (!encoder.matches(password, user.getPassword())) {
      throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
    }
    return new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
  }
}
