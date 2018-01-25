package com.jiramot.auth.security.auth.username;

import com.jiramot.auth.security.model.UserContext;
import com.jiramot.auth.user.model.User;
import com.jiramot.auth.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsernameAuthenticationProvider implements AuthenticationProvider {

  private UserService userService;
  private BCryptPasswordEncoder encoder;

  @Autowired
  public UsernameAuthenticationProvider(UserService userService, BCryptPasswordEncoder encoder) {
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
    if (user.getRoles() == null) throw new InsufficientAuthenticationException("User has no roles assigned");
    List<GrantedAuthority> authorities = user.getRoles().stream()
        .map(authority -> new SimpleGrantedAuthority(authority))
        .collect(Collectors.toList());
    UserContext userContext = UserContext.create(user.getUsername(), user.getUuid(), authorities);
    return new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
  }
}
