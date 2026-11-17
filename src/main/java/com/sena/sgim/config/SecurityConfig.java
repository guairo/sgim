package com.sena.sgim.config;

import com.sena.sgim.security.UsuarioDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuracion de seguridad: BCrypt, formulario de login y autorizacion por rol.
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UsuarioDetailsService usuarioDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(10); }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(usuarioDetailsService);
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authProvider())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/webjars/**", "/favicon.ico", "/login", "/error").permitAll()
                .requestMatchers("/usuarios/**", "/auditoria/**").hasRole("ADMIN")
                .requestMatchers("/bienes/eliminar/**", "/dependencias/eliminar/**",
                                 "/categorias/eliminar/**", "/proveedores/eliminar/**",
                                 "/funcionarios/eliminar/**").hasAnyRole("ADMIN")
                .requestMatchers("/bienes/nuevo", "/bienes/editar/**", "/bienes/asignar/**",
                                 "/movimientos/**", "/mantenimientos/**")
                    .hasAnyRole("ADMIN", "OPERADOR")
                .anyRequest().authenticated())
            .formLogin(f -> f
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .permitAll())
            .logout(l -> l
                .logoutSuccessUrl("/login?logout")
                .permitAll())
            .csrf(csrf -> {});
        return http.build();
    }
}
