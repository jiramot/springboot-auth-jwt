package com.jiramot.auth.controller;

import com.jiramot.auth.security.TokenResponse;
import com.jiramot.auth.security.WebSecurityConfig;
import com.jiramot.auth.security.auth.jwt.JwtHeaderTokenExtractor;
import com.jiramot.auth.security.auth.jwt.TokenVerifier;
import com.jiramot.auth.security.config.JwtSettings;
import com.jiramot.auth.security.exception.InvalidJwtToken;
import com.jiramot.auth.security.model.UserContext;
import com.jiramot.auth.security.model.token.JwtToken;
import com.jiramot.auth.security.model.token.JwtTokenFactory;
import com.jiramot.auth.security.model.token.RawAccessJwtToken;
import com.jiramot.auth.security.model.token.RefreshToken;
import com.jiramot.auth.user.model.User;
import com.jiramot.auth.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RefreshTokenController {


  @Autowired
  private JwtTokenFactory tokenFactory;

  @Autowired
  private JwtSettings jwtSettings;

  @Autowired
  private UserService userService;

  @Autowired
  private TokenVerifier tokenVerifier;
  @Autowired
  private JwtHeaderTokenExtractor tokenExtractor;

  @RequestMapping(value = "/refresh", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
  public @ResponseBody
  TokenResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    String tokenPayload = tokenExtractor.extract(request.getHeader(WebSecurityConfig.AUTHENTICATION_HEADER_NAME));

    RawAccessJwtToken rawToken = new RawAccessJwtToken(tokenPayload);
    RefreshToken refreshToken = RefreshToken.create(rawToken, jwtSettings.getTokenSigningKey()).orElseThrow(() -> new InvalidJwtToken());

    String jti = refreshToken.getJti();
    if (!tokenVerifier.verify(jti)) {
      throw new InvalidJwtToken();
    }

    String subject = refreshToken.getSubject();
    User user = userService.findByUsername(subject).orElseThrow(() -> new UsernameNotFoundException("User not found: " + subject));

    if (user.getRoles() == null) throw new InsufficientAuthenticationException("User has no roles assigned");
    List<GrantedAuthority> authorities = user.getRoles().stream()
        .map(authority -> new SimpleGrantedAuthority(authority))
        .collect(Collectors.toList());
    UserContext userContext = UserContext.create(user.getUsername(), user.getUuid(), authorities);

    JwtToken accessJwtToken = tokenFactory.createAccessJwtToken(userContext);
    JwtToken refreshJwtToken = tokenFactory.createRefreshToken(userContext);

    return new TokenResponse(accessJwtToken.getToken(), refreshJwtToken.getToken());
  }
}
