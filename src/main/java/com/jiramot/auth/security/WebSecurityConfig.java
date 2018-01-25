package com.jiramot.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiramot.auth.security.auth.jwt.JwtAuthenticationProvider;
import com.jiramot.auth.security.auth.jwt.JwtHeaderTokenExtractor;
import com.jiramot.auth.security.auth.jwt.JwtTokenAuthenticationProcessingFilter;
import com.jiramot.auth.security.auth.jwt.SkipPathRequestMatcher;
import com.jiramot.auth.security.auth.username.UsernameAuthenticationFilter;
import com.jiramot.auth.security.auth.username.UsernameAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

import static com.jiramot.auth.security.SecurityConstants.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  public static final String AUTHENTICATION_HEADER_NAME = "Authorization";


  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;
  @Autowired
  private UsernameAuthenticationProvider usernameAuthenticationProvider;
  @Autowired
  private JwtAuthenticationProvider jwtAuthenticationProvider;

  @Autowired
  private AuthenticationFailureHandler failureHandler;
  @Autowired
  private AuthenticationSuccessHandler successHandler;
  @Autowired
  private JwtHeaderTokenExtractor tokenExtractor;
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private ObjectMapper objectMapper;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    List<String> permitAllEndpointList = Arrays.asList(
        LOGIN_URL,
        SIGN_UP_URL,
        REFRESH_TOKEN_URL
    );

    http.csrf().disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers(permitAllEndpointList.toArray(new String[permitAllEndpointList.size()]))
        .permitAll()
        .anyRequest().authenticated()
        .and()
        .addFilterBefore(createUsernameAuthenticationFIlter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(createJwtAuthenticationFilter(permitAllEndpointList, ROOT_API_URL),
            UsernamePasswordAuthenticationFilter.class);

  }

  UsernameAuthenticationFilter createUsernameAuthenticationFIlter() {
    UsernameAuthenticationFilter filter = new UsernameAuthenticationFilter(LOGIN_URL, successHandler, failureHandler, objectMapper);
    filter.setAuthenticationManager(this.authenticationManager);
    return filter;
  }

  JwtTokenAuthenticationProcessingFilter createJwtAuthenticationFilter(List<String> pathsToSkip, String pattern) {
    SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, pattern);
    JwtTokenAuthenticationProcessingFilter filter = new JwtTokenAuthenticationProcessingFilter(matcher, failureHandler, tokenExtractor);
    filter.setAuthenticationManager(this.authenticationManager);
    return filter;
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(usernameAuthenticationProvider);
    auth.authenticationProvider(jwtAuthenticationProvider);
  }
}
