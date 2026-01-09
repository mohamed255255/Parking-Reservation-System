package com.parking_reservation_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.parking_reservation_system.security.JWTFilter;
import com.parking_reservation_system.service.CustomUserDetailsService;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    private final CustomUserDetailsService customUserDetailsService;
    private final JWTFilter JwtFilter ;
    
    public SecurityConfiguration( CustomUserDetailsService customUserDetailsService  , JWTFilter JwtFilter) 
    {
        this.customUserDetailsService = customUserDetailsService;
        this.JwtFilter = JwtFilter ;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
        .requestMatchers(
            "/register",
            "/login",
            "/verify-user",
            "/v3/api-docs/**",
            "/swagger-ui/**",  
            "/swagger-ui.html" , 
            "/api/webhook/**"  ,
            "/test/pay"

        ).permitAll().anyRequest().authenticated()
        )
        .addFilterBefore(JwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
        
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService); 
        provider.setPasswordEncoder(passwordEncoder());       
        return provider;
    }
 
    @Bean
    public  AuthenticationManager AuthenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }


}
