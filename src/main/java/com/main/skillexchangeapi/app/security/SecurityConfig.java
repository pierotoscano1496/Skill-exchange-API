package com.main.skillexchangeapi.app.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Autowired
    private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JWTAuthorizationFilter jwtAuthorizationFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private Environment environment;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity,
            AuthenticationManager authenticationManager) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/simple-check").permitAll()
                        .requestMatchers(HttpMethod.POST, "/simple-check").permitAll()
                        // .requestMatchers(HttpMethod.POST, "/usuario").permitAll()
                        .requestMatchers(HttpMethod.GET, "/testing/ex/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuario").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/categoria").permitAll()
                        .requestMatchers(HttpMethod.GET, "/categoria/details").permitAll()
                        .requestMatchers(HttpMethod.GET, "/sub-categoria").permitAll()
                        .requestMatchers(HttpMethod.GET, "/sub-categoria/categoria/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/skill").permitAll()
                        .requestMatchers(HttpMethod.GET, "/skill/sub-categoria/*").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/usuario/skills/*").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/usuario/plan/*").permitAll()

                        // Check: Encontrar manera de establecerlo privado:
                        .requestMatchers(HttpMethod.GET, "/messaging-socket/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/servicio/details/preview/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/servicio/busqueda").permitAll()
                        .requestMatchers(HttpMethod.GET, "/servicios/review/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/chat-resources/upload").permitAll()
                        .requestMatchers("/error").anonymous()
                        // Swagger
                        .requestMatchers(HttpMethod.GET, "/swagger-ui-custom.html").permitAll()
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                // .exceptionHandling(exception ->
                // exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        String[] activeProfiles = environment.getActiveProfiles();
        boolean isProd = false;
        for (String profile : activeProfiles) {
            if ("prod".equals(profile)) {
                isProd = true;
                break;
            }
        }

        if (isProd) {
            configuration.setAllowedOrigins(Arrays.asList("http://tuchambita.com"));
            configuration.setAllowCredentials(false);
        } else {
            configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
            configuration.setAllowCredentials(true);
        }

        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "DELETE", "PUT", "OPTIONS"));
        configuration.setMaxAge(3600L);
        configuration
                .setAllowedHeaders(Arrays.asList("Accept", "Content-Type", "Origin", "Authorization", "X-Auth-Token"));
        configuration.setExposedHeaders(Arrays.asList("X-Auth-Token", "Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authProvider())
                .build();
    }
}