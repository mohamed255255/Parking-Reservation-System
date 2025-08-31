package com.garage_system.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.garage_system.Service.CustomUserDetailsService;

@Configuration
public class SecurityConfiguration {

    private final CustomUserDetailsService customUserDetailsService;
    
    private final JWTFilter JwtFilter ;
    
    public SecurityConfiguration(CustomUserDetailsService customUserDetailsService  , JWTFilter JwtFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.JwtFilter = JwtFilter ;
    }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
              .csrf(csrf -> csrf.disable())
              .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
              .authorizeHttpRequests(auth -> auth
                   .requestMatchers("/Register" , "/login").permitAll()  /// white list for guest users
                   .anyRequest().authenticated()
              )
              .addFilterBefore(JwtFilter, UsernamePasswordAuthenticationFilter.class)
              .httpBasic(withDefaults());

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
