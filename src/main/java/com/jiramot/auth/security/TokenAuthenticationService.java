package com.jiramot.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.jiramot.auth.security.SecurityConstants.*;
import static java.util.Collections.emptyList;

class TokenAuthenticationService {

  static final String TOKEN_PREFIX = "Bearer";
  static final String HEADER_STRING = "Authorization";

  private static final ObjectMapper objectMapper = new ObjectMapper();

  static void addAuthentication(HttpServletResponse res, String username) throws IOException {
    LocalDateTime currentTime = LocalDateTime.now();

    Claims claims = Jwts.claims().setSubject(username);


    String accessToken = generateAccessToken(currentTime, claims);
    String refreshToken = generateAccessToken(currentTime, claims);

    Map<String, String> tokenMap = new HashMap<String, String>();
    tokenMap.put("access_token", accessToken);
    tokenMap.put("refresh_token", refreshToken);
    String expireIn = "" + (EXPIRATION_TIME_IN_MINUTE * 60 * 1000);
    tokenMap.put("expire_in", expireIn);
    tokenMap.put("token_type", "bearer");

    res.setStatus(HttpStatus.OK.value());
    res.setContentType(MediaType.APPLICATION_JSON_VALUE);
    objectMapper.writeValue(res.getWriter(), tokenMap);
  }

  private static String generateAccessToken(LocalDateTime currentTime, Claims claims) {
    return Jwts.builder()
        .setClaims(claims)
        .setIssuer(ISSUEER)
        .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
        .setExpiration(Date.from(currentTime
            .plusMinutes(EXPIRATION_TIME_IN_MINUTE)
            .atZone(ZoneId.systemDefault()).toInstant()))
        .signWith(SignatureAlgorithm.HS512, SECRET)
        .compact();
  }

  private static String generateRefreshToken(LocalDateTime currentTime, Claims claims) {
    return Jwts.builder()
        .setClaims(claims)
        .setIssuer(ISSUEER)
        .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
        .setExpiration(Date.from(currentTime
            .plusMinutes(REFRESH_TIME_IN_MINUTE)
            .atZone(ZoneId.systemDefault()).toInstant()))
        .signWith(SignatureAlgorithm.HS512, SECRET)
        .compact();
  }

  static Authentication getAuthentication(HttpServletRequest request) {
    String token = request.getHeader(HEADER_STRING);

    if (token != null) {

      Jws<Claims> claimsJws = Jwts.parser()
          .setSigningKey(SECRET)
          .parseClaimsJws(token.replace(TOKEN_PREFIX, ""));
      String user = claimsJws.getBody().getSubject();

      if (user != null) {
        return new UsernamePasswordAuthenticationToken(user, null, emptyList());
      }
    }
    return null;
  }
}