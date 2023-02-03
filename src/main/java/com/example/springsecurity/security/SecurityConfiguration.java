package com.example.springsecurity.security;

import com.example.springsecurity.security.filter.AuthTokenFilter;
import com.example.springsecurity.security.filter.MyFilter;
import com.example.springsecurity.security.filter.TestFilter;
import com.example.springsecurity.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final UserDetailsServiceImpl userDetailsService;
  private final AuthTokenFilter authTokenFilter;
  private final TestFilter testFilter;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  @Primary
  public AuthenticationManagerBuilder configureAuthenticationManagerBuilder(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    return authenticationManagerBuilder;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors()
        .and()
          .csrf().disable().authorizeHttpRequests()
          .requestMatchers("/signin").permitAll()
          .requestMatchers("/signup").permitAll()
          .requestMatchers("/users").hasRole("ADMIN")
          .anyRequest().authenticated()
        .and()
          .formLogin().defaultSuccessUrl("/profile")
    ;

    http.addFilterBefore(testFilter, UsernamePasswordAuthenticationFilter.class);
    http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class); // JWT 인증 Filter
    http.addFilterAfter(new MyFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
