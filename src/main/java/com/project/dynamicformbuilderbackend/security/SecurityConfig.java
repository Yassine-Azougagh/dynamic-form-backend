package com.project.dynamicformbuilderbackend.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SecurityConfig {
    private static final List<String> WHITELISTED_API_ENDPOINTS = Arrays.asList("/auth/signup", "/auth/login", "/auth/refresh-token", "/swagger-ui/**", "/v3/api-docs/**" );
    private static final List<String> ALLOWED_ORIGINS = Arrays.asList("http://localhost:5173/" );

    private final Logger log= LoggerFactory.getLogger(SecurityConfig.class);
    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    public SecurityConfig(TokenAuthenticationFilter tokenAuthenticationFilter) {
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
    }

// standard constructor

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        var allPermittedPaths = new ArrayList<>(WHITELISTED_API_ENDPOINTS);

        log.info("permitted endpoints {}", allPermittedPaths);
        return http
                .csrf(AbstractHttpConfigurer::disable)
//                .cors(Customizer.withDefaults())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                        .xssProtection(HeadersConfigurer.XXssConfig::disable)
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'; frame-ancestors 'none';")))

                .authorizeHttpRequests(auth -> {
                    allPermittedPaths.forEach(path -> auth.requestMatchers(path).permitAll());
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .build();

    }

    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Use configured origins instead of wildcard
        configuration.setAllowedOrigins(ALLOWED_ORIGINS);
        // Allow common HTTP methods
        configuration.setAllowedMethods(List.of(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.HEAD.name()
        ));

        // Allow specific headers
        configuration.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept",
                "Origin",
                "X-Requested-With",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
        ));

        // Only enable credentials if not using wildcard origin
        configuration.setAllowCredentials(!ALLOWED_ORIGINS.contains("*"));
        configuration.setMaxAge(3600L); // Cache preflight requests for 1 hour

        log.info("Configuring CORS with {} allowed origins", ALLOWED_ORIGINS.size());
        log.debug("CORS configuration details: origins={}, methods={}, headers={}",
                ALLOWED_ORIGINS,
                configuration.getAllowedMethods(),
                configuration.getAllowedHeaders());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
