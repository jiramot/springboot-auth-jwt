package com.jiramot.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

import static com.jiramot.auth.security.SecurityConstants.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;
  @Autowired
  private JwtAuthenticationProvider authenticationProvider;
  @Autowired
  private AuthenticationFailureHandler failureHandler;

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
        .addFilterBefore(new JwtLoginFilter(LOGIN_URL, authenticationManager()),
            UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(createJwtAuthenticationFilter(permitAllEndpointList, "/**"),
            UsernamePasswordAuthenticationFilter.class);

  }

  JwtAuthenticationFilter createJwtAuthenticationFilter(List<String> pathsToSkip, String pattern) {
    SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, pattern);
    return new JwtAuthenticationFilter(matcher, failureHandler);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authenticationProvider);

  }
}
