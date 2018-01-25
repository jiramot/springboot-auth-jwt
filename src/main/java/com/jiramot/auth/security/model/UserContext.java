package com.jiramot.auth.security.model;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
public class UserContext {
  private final String username;
  private final String uuid;
  private final List<GrantedAuthority> authorities;

  private UserContext(String username, String uuid, List<GrantedAuthority> authorities) {
    this.username = username;
    this.uuid = uuid;
    this.authorities = authorities;
  }

  public static UserContext create(String username, String uuid, List<GrantedAuthority> authorities) {
    if (StringUtils.isBlank(username)) throw new IllegalArgumentException("Username is blank: " + username);
    if (StringUtils.isBlank(uuid)) throw new IllegalArgumentException("Username is blank: " + uuid);
    return new UserContext(username, uuid, authorities);
  }
}
