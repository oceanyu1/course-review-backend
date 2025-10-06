package com.oceancode.coursereview.config;

import com.oceancode.coursereview.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()                    // Anyone can log in/register
                        .requestMatchers(HttpMethod.GET, "/api/courses/**").permitAll() // Anyone can view courses
                        .requestMatchers(HttpMethod.GET, "/api/departments").permitAll() // Anyone can see departments
                        .requestMatchers(HttpMethod.GET, "/api/reviews/**").permitAll()   // <- allow public GETs
                        .requestMatchers(HttpMethod.POST, "/api/reviews/**").authenticated() // POST still needs auth
                        .requestMatchers(HttpMethod.PUT, "/api/reviews/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/reviews/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/reviews/**").authenticated()
                        .anyRequest().authenticated()                                     // Everything else needs auth
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "http://localhost:4173",
                "https://course-review-frontend-ivory.vercel.app",
                "https://www.ravensrate.ca",
                "https://ravensrate.ca"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean(CorsConfigurationSource corsConfigurationSource) {
        // NOTE: Requires import of org.springframework.web.filter.CorsFilter and org.springframework.boot.web.servlet.FilterRegistrationBean
        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource));
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
