package com.app.travel.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.app.travel.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private AuthEntryPointJwt authEntryPoint;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .headers(headers -> headers
                .frameOptions(frame -> frame.deny())
                .contentTypeOptions(content -> content.disable())
                .httpStrictTransportSecurity(hstsConfig -> hstsConfig.disable()))
            .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Swagger/OpenAPI endpoints - Configuration pour context-path /travel
                .requestMatchers("/v3/api-docs/**", "/v3/api-docs").permitAll()
                .requestMatchers("/travel/v3/api-docs/**", "/travel/v3/api-docs").permitAll()
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/travel/swagger-ui/**", "/travel/swagger-ui.html").permitAll()
                .requestMatchers("/swagger-resources/**").permitAll()
                .requestMatchers("/webjars/**").permitAll()
                .requestMatchers("/docs/**", "/swagger/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                      
                // Endpoints publics d'authentification
                .requestMatchers("/auth/**", "/test/**").permitAll()
                .requestMatchers("/password-recovery/**").permitAll()
                          
                // Endpoints pour les ressources statiques
                .requestMatchers("/static/**", "/css/**", "/js/**", "/images/**").permitAll()
                
                // Trips - Endpoints publics (lecture seule pour le front)
                .requestMatchers(HttpMethod.GET, "/trips").permitAll()
                .requestMatchers(HttpMethod.GET, "/trips/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/trips/continent/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/trips/country/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/trips/city/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/trips/search/**").permitAll()
                
                // Options - Public en lecture, sécurisé en écriture
                .requestMatchers(HttpMethod.GET, "/options").permitAll()
                .requestMatchers(HttpMethod.GET, "/options/**").permitAll()


                // Cache - Seulement pour les admins
                .requestMatchers("/cache/**").hasRole("ADMIN")

                //Tous les autres endpoints nécessitent une authentification ou pour etre plus explicite
                .requestMatchers("/travel/**").authenticated()

                //Tous les autres requêtes nécessitent une authentification
                .anyRequest().authenticated()
            );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
