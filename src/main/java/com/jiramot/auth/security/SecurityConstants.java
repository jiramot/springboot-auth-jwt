package com.jiramot.auth.security;

public class SecurityConstants {
  static final String LOGIN_URL = "/login";
  static final String SIGN_UP_URL = "/signup";
  static final String REFRESH_TOKEN_URL = "/refresh";

  static final String SECRET = "SECRET";
  static final long EXPIRATION_TIME_IN_MINUTE = 15;
  static final long REFRESH_TIME_IN_MINUTE = 60 * 24 * 30;
  static final String ISSUEER = "AUTH";
}
