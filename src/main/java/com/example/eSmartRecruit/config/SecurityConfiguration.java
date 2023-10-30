package com.example.eSmartRecruit.config;

import com.example.eSmartRecruit.models.enumModel.Role;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf-> csrf.disable())
                .authorizeHttpRequests(auth->auth
                                            .requestMatchers("/eSmartRecruit/**")
                                            .permitAll() //let all request pass to the above URL, no authen yet
                                            .requestMatchers("/eSmartRecruit/application/create/**")
                                            .hasAnyAuthority(Role.Candidate.name())//only candidate can get access to the link above
                                            .anyRequest()
                                            .authenticated())
                .sessionManagement(session -> session
                                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                            .authenticationProvider(authenticationProvider)
                                            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
