package com.jiramot.auth;

public enum Scopes {
  REFRESH_TOKEN;

  public String authority() {
    return this.name();
  }
}
