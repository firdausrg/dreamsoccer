package com.promptengineer.dreamsoccer.security;

import com.promptengineer.dreamsoccer.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.stream.Collectors;

@Configuration
public class SecurityConfiguration {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(SecurityPage.PUBLIC_URLS).permitAll()
                        .requestMatchers(SecurityPage.ADMIN_URLS).hasRole("ADMIN")
                        .requestMatchers(SecurityPage.USER_URLS).hasRole("USER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .successHandler((request, response, authentication) -> {
                            String role = authentication.getAuthorities().stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .collect(Collectors.joining());

                            String redirectUrl = "/dashboard";

                            if (role.contains("ROLE_ADMIN")) {
                                redirectUrl = "/admin_dashboard";
                            } else if (role.contains("ROLE_USER")) {
                                redirectUrl = "/user_dashboard";
                            }

                            response.sendRedirect("/auth/login?success=true&redirectUrl=" + redirectUrl);
                        })
                        .failureHandler((request, response, exception) -> {
                            String errorMessage = "Username atau password salah!";

                            if (exception instanceof InternalAuthenticationServiceException) {
                                errorMessage = "Akun Anda tidak aktif. Silakan hubungi admin.";
                            } else if (exception instanceof BadCredentialsException) {
                                errorMessage = "Username atau password salah!";
                            }

                            response.sendRedirect("/auth/login?error=true&message=" + errorMessage);
                        })
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/login?logout=true")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/404")
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, CustomUserDetailsService userDetailsService) throws Exception {
        return http
                .getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }
}
