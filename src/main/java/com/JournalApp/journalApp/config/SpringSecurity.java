package com.JournalApp.journalApp.config;

import com.JournalApp.journalApp.Service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Autowired
    private UserDetailServiceImpl userDetailService;

    // 1. Replaces the configure(HttpSecurity http) method
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request
                        .requestMatchers("/Journal/**").authenticated() // Fixed typo and updated method name
                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults()) // New Lambda syntax
                .csrf(AbstractHttpConfigurer::disable); // New Lambda syntax

        return http.build();
    }

    // 2. Replaces the configure(AuthenticationManagerBuilder auth) method
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        // Pass the userDetailService directly into the constructor!
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailService);

        // We still set the password encoder the same way
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    // 3. Standard BCrypt Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}