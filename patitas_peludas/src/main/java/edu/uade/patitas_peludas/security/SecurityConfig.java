package edu.uade.patitas_peludas.security;

import edu.uade.patitas_peludas.jwt.JwtAthFilter;
import edu.uade.patitas_peludas.service.implementation.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtAthFilter jwtAuthFilter;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors((httpSecurityCorsConfigurer -> corsConfigurationSource()))
                .authorizeHttpRequests((authorize) ->
                        authorize
                                // delete user
                                .requestMatchers(HttpMethod.DELETE, "/api/user/**").hasAuthority("ADMIN")
                                // change status
                                .requestMatchers(HttpMethod.PATCH, "/api/user/**").hasAuthority("ADMIN")
                                // get all users
                                .requestMatchers(HttpMethod.GET, "/api/user").hasAuthority("ADMIN")
                                // create product
                                .requestMatchers(HttpMethod.POST, "/api/product").hasAuthority("VENDOR")
                                // edit product
                                .requestMatchers(HttpMethod.PUT, "/api/product/**").hasAuthority("VENDOR")
                                // delete product
                                .requestMatchers(HttpMethod.DELETE, "/api/product/**").hasAuthority("VENDOR")
                                // create invoice (buy a product)
                                .requestMatchers(HttpMethod.POST, "/api/invoice").hasAnyAuthority("VENDOR", "BUYER")
                                // get invoice by id and user id
                                .requestMatchers(HttpMethod.GET, "/api/invoice/**").hasAnyAuthority("VENDOR", "BUYER")
                        .anyRequest().permitAll())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(daoAuthenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
