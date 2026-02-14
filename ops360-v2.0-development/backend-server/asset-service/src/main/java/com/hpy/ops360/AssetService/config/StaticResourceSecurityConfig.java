package com.hpy.ops360.AssetService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class StaticResourceSecurityConfig {

    @Bean
    public SecurityFilterChain staticResourceFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/images/**")
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            .requestCache().disable()
            .securityContext().disable()
            .sessionManagement().disable()
            .csrf().disable();

        return http.build();
    }
}

