package com.ezen.www.config;

import com.ezen.www.security.CustomUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  /* springSecurity6 => createDelgationPasswordEncoder */

  @Bean
  PasswordEncoder passwordEncoder(){
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  //SecurityFilterChain 객체로 설정
  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
    return http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/","/index","/js/**","/dist/**","/board/list", "/member/login","/member/register","/upload/**","/comment/**").permitAll()
            .requestMatchers("/member/list").hasAnyRole("ADMIN")
            .anyRequest().authenticated()
        )
        .formLogin(login -> login
            .usernameParameter("email")
            .passwordParameter("pwd")
            .loginPage("/member/login")
            .defaultSuccessUrl("/board/list").permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/member/logout")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .logoutSuccessUrl("/")
        ).build();
    // return http.build();
  }

  //userDetailsService : spring에서 만든 클래스와 같은 객체
  @Bean
  UserDetailsService userDetailsService(){
    return new CustomUserService();   // Security 패키지에 클래스로 생성
  }

  //authenticationManger 객체
  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }













}
