package com.jiramot.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

import static com.jiramot.auth.security.SecurityConstants.LOGIN_URL;
import static com.jiramot.auth.security.SecurityConstants.SIGN_UP_URL;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private JWTAuthenticationProvider jwtAuthenticationProvider;
  private AuthenticationManager authenticationManager;
  private ObjectMapper objectMapper;
  private JWTAuthenticationFailureHandler failureHandler;
  private JWTAuthenticationSuccessHandler successHandler;
  private UserDetailsService userDetailsService;
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public WebSecurityConfig(JWTAuthenticationProvider jwtAuthenticationProvider, AuthenticationManager authenticationManager, ObjectMapper objectMapper, JWTAuthenticationFailureHandler failureHandler, JWTAuthenticationSuccessHandler successHandler, UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.jwtAuthenticationProvider = jwtAuthenticationProvider;
    this.authenticationManager = authenticationManager;
    this.objectMapper = objectMapper;
    this.failureHandler = failureHandler;
    this.successHandler = successHandler;
    this.userDetailsService = userDetailsService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(jwtAuthenticationProvider);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    List<String> permitAllEndpointList = Arrays.asList(
        LOGIN_URL,
        SIGN_UP_URL
    );
    http.csrf().disable()
        .authorizeRequests()
        .antMatchers(permitAllEndpointList.toArray(new String[permitAllEndpointList.size()]))
        .permitAll()
        .anyRequest().authenticated()
        .and()
        .addFilterBefore(buildJwtProcessingFilter(LOGIN_URL), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

  }


  protected JWTLoginFilter buildJwtProcessingFilter(String loginEntryPoint) throws Exception {
    JWTLoginFilter filter = new JWTLoginFilter(loginEntryPoint, successHandler, failureHandler, objectMapper);
    filter.setAuthenticationManager(this.authenticationManager);
    return filter;
  }
}
