package com.example.hanaservizi_e.config;

import com.example.hanaservizi_e.CustomOidcUserService;
import com.example.hanaservizi_e.service.CustomUserDetailsService;
import com.example.hanaservizi_e.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomOAuth2UserService oAuth2UserService;
    private final CustomOidcUserService oidcUserService; // NUEVO

    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                          CustomOAuth2UserService oAuth2UserService,
                          CustomOidcUserService oidcUserService) {
        this.customUserDetailsService = customUserDetailsService;
        this.oAuth2UserService = oAuth2UserService;
        this.oidcUserService = oidcUserService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            if (exception instanceof DisabledException) {
                response.sendRedirect("/cuenta-desactivada");
            } else {
                response.sendRedirect("/login?error=true");
            }
        };
    }

    // SecurityFilterChain actualizado
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/usuarios/registro", "/admin/usuarios/enviar-correo-masivo", "/admin/usuarios/mensajes-masivos", "/api/ws/users/**", "/api/dashboard/**", "/stripe/**")
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/login", "/usuarios/registro", "/usuarios/registro/**",
                                "/CSS/**", "/JS/**", "/img/**", "/fondos/**", "/error", "/redireccionar-por-rol", "/productos/**",
                                "/uploads/**", "/filtros", "/api/chat/**", "/cuenta-desactivada", "/Notificaciones/**","/api/ws/users/**", "/api/dashboard/**","/stripe/**"
                        ).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/vendedor/**").hasAnyRole("VENDEDOR", "ADMIN")
                        .requestMatchers("/cliente/**").hasAnyRole("CLIENTE", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/redireccionar-por-rol", true)
                        .failureUrl("/cuenta-desactivada")
                        .permitAll()
                ).oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .failureHandler(authenticationFailureHandler())
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService)
                                .oidcUserService(oidcUserService)
                        )
                        .defaultSuccessUrl("/redireccionar-por-rol", true)
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(handling -> handling
                        .accessDeniedPage("/access_denied")
                );

        return http.build();
    }

}