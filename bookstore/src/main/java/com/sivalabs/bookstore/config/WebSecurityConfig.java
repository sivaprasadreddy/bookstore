package com.sivalabs.bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

@Configuration
@EnableWebSecurity
class WebSecurityConfig {
    private static final String[] PUBLIC_RESOURCES = {
        "/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico", "/actuator/**", "/error",
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        String[] unsecuredPaths = {
            "/login", "/registration", "/registration/success", "/add-to-cart",
        };
        http.securityMatcher("/**");

        http.csrf(CsrfConfigurer::disable);
        http.authorizeHttpRequests(r -> r.requestMatchers(PUBLIC_RESOURCES)
                .permitAll()
                .requestMatchers(unsecuredPaths)
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/products/**")
                .permitAll()
                .anyRequest()
                .authenticated());

        http.formLogin(formLogin -> formLogin.loginPage("/login").permitAll());

        http.logout(logout -> logout.logoutRequestMatcher(
                        PathPatternRequestMatcher.withDefaults().matcher("/logout"))
                .logoutSuccessUrl("/")
                .permitAll());

        return http.build();
    }
}
