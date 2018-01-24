package com.jiramot.auth.security.model;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class UserContext {
  @Getter
  private String username;
  @Getter
  private final List<GrantedAuthority> authorities;


  private UserContext(String username, List<GrantedAuthority> authorities) {
    this.username = username;
    this.authorities = authorities;
  }

  public static UserContext create(String username) {
    if (StringUtils.isBlank(username)) throw new IllegalArgumentException("Username is blank: " + username);
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("READ"));
    return new UserContext(username, authorities);
  }
}
