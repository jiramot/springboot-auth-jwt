package com.jiramot.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiramot.auth.Scopes;
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
import java.util.*;

import static com.jiramot.auth.security.SecurityConstants.*;
import static java.util.Collections.emptyList;

public class TokenAuthenticationService {

  static final String TOKEN_PREFIX = "Bearer";
  static final String HEADER_STRING = "Authorization";


  private static final ObjectMapper objectMapper = new ObjectMapper();

  static void addAuthentication(HttpServletResponse res, String username) throws IOException {


    String accessToken = generateAccessToken(username);
    String refreshToken = generateRefreshToken(username);

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

  private static String generateAccessToken(String username) {
    LocalDateTime currentTime = LocalDateTime.now();
    Claims claims = Jwts.claims().setSubject(username);
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

  private static String generateRefreshToken(String username) {
    LocalDateTime currentTime = LocalDateTime.now();
    Claims claims = Jwts.claims().setSubject(username);
    claims.put("scopes", Arrays.asList(Scopes.REFRESH_TOKEN.authority()));
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
      List<String> scopes = claimsJws.getBody().get("scopes", List.class);


      String subject = claimsJws.getBody().getSubject();

      if (subject != null && hasNoScope(scopes)) {
        return new UsernamePasswordAuthenticationToken(subject, null, emptyList());
      }
    }
    return null;
  }

  private static boolean hasNoScope(List<String> scopes) {
    return scopes == null || scopes.isEmpty() || !scopes.stream().filter(scope -> Scopes.REFRESH_TOKEN.authority().equals(scope)).findFirst().isPresent();
  }

  public static void refresh(HttpServletRequest req, HttpServletResponse res) throws IOException {
    String refreshToken = req.getHeader(HEADER_STRING);

    if (refreshToken != null) {
      Jws<Claims> claimsJws = Jwts.parser()
          .setSigningKey(SECRET)
          .parseClaimsJws(refreshToken.replace(TOKEN_PREFIX, ""));
      List<String> scopes = claimsJws.getBody().get("scopes", List.class);

      if (hasRefreshScope(scopes)) {

        String username = claimsJws.getBody().getSubject();

        if (username != null) {

          LocalDateTime currentTime = LocalDateTime.now();


          String accessToken = generateAccessToken(username);
          String newRefreshToken = generateRefreshToken(username);

          Map<String, String> tokenMap = new HashMap<String, String>();
          tokenMap.put("access_token", accessToken);
          tokenMap.put("refresh_token", newRefreshToken);
          String expireIn = "" + (EXPIRATION_TIME_IN_MINUTE * 60 * 1000);
          tokenMap.put("expire_in", expireIn);
          tokenMap.put("token_type", "bearer");

          res.setStatus(HttpStatus.OK.value());
          res.setContentType(MediaType.APPLICATION_JSON_VALUE);
          objectMapper.writeValue(res.getWriter(), tokenMap);
        }
      }
    }

    res.setStatus(HttpStatus.UNAUTHORIZED.value());
  }

  private static boolean hasRefreshScope(List<String> scopes) {
    return scopes != null && !scopes.isEmpty() && scopes.stream().filter(scope -> Scopes.REFRESH_TOKEN.authority().equals(scope)).findFirst().isPresent();
  }
}