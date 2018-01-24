package com.jiramot.auth.security;

import com.jiramot.auth.security.model.UserContext;
import com.jiramot.auth.user.model.User;
import com.jiramot.auth.user.service.DatabaseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class JWTAuthenticationProvider implements AuthenticationProvider {
  private final BCryptPasswordEncoder encoder;
  private DatabaseUserService userService;

  @Autowired
  public JWTAuthenticationProvider(final DatabaseUserService userService, final BCryptPasswordEncoder encoder) {
    this.userService = userService;
    this.encoder = encoder;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = (String) authentication.getPrincipal();
    String password = (String) authentication.getCredentials();

    User user = userService.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    if (!encoder.matches(password, user.getPassword())) {
      throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
    }

    UserContext userContext = UserContext.create(user.getUsername());

    return new UsernamePasswordAuthenticationToken(userContext, null);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
  }
}
