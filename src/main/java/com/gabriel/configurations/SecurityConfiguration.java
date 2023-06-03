package com.gabriel.configurations;

import com.gabriel.security.authentication.CustomAuthenticationEntryPoint;
import com.gabriel.security.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final JwtFilter jwtFilter;

    @Value("${app.jwt.token.cookie.name}")
    private String TOKEN_COOKIE_NAME;

    public SecurityConfiguration(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * Configura y devuelve el AuthenticationManager para autenticar usuarios.
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http,
                                             PasswordEncoder passwordEncoder,
                                             UserDetailsService userDetailsService) throws Exception {

        return http
                    .getSharedObject(AuthenticationManagerBuilder.class)
                    .userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder)
                    .and()
                    .build();

    }
    /**
     * Configura y devuelve el SecurityFilterChain para la gestión de seguridad HTTP.
     * Define las reglas de autorización para las diferentes rutas y solicitudes.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/logout").authenticated()
                .requestMatchers("/error").permitAll()
                .requestMatchers("/static/**").permitAll()
                .requestMatchers("/assets/**").permitAll()
                .requestMatchers("/webjars/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/admin").hasRole("ADMIN")
                .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/event/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/ticket/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/operator/**").hasRole("OPERATOR")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/error")
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies(TOKEN_COOKIE_NAME)
                .logoutSuccessUrl("/login?logout")
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    /**
     * Devuelve el PasswordEncoder para cifrar y comparar contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
