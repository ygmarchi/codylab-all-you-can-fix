package it.intesys.orderservice.configuration;

import org.springdoc.core.utils.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable() // Disable CSRF protection
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(Constants.SWAGGER_UI_PREFIX + "/**").permitAll()
                        .requestMatchers(Constants.DEFAULT_API_DOCS_URL + "/**").permitAll()
                        .requestMatchers("/api/orders").permitAll()
                        .requestMatchers("/api/orders/bulk").permitAll()
                        .anyRequest().authenticated());

        return http.build();
    }
}
