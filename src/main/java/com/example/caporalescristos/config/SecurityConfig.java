package com.example.caporalescristos.config;

import com.example.caporalescristos.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    /** Rutas públicas (sin JWT) */
    private static final String[] PUBLIC_PATHS = {
            "/api/auth/**",
            "/api/fotos",
            "/api/fotos/**",
            "/api/secciones-galeria",
            "/api/secciones-galeria/**",
            "/api/videos",
            "/api/videos/**",
            "/api/productos",
            "/api/productos/**",
            "/api/categorias",
            "/api/categorias/**",
            "/api/configuracion-tienda",
            "/api/configuracion-inicio",
            "/api/metodos-pago",
            "/api/pedidos",
            "/uploads/**",
            "/error"
    };

    /** Rutas solo para ADMIN (excluye /api/admin/login para que sea público) */
    private static final String[] ADMIN_PATHS = {
            "/api/admin/fotos", "/api/admin/fotos/**",
            "/api/admin/secciones-galeria", "/api/admin/secciones-galeria/**",
            "/api/admin/videos", "/api/admin/videos/**",
            "/api/admin/productos", "/api/admin/productos/**",
            "/api/admin/categorias", "/api/admin/categorias/**",
            "/api/admin/configuracion-tienda", "/api/admin/configuracion-tienda/**",
            "/api/admin/configuracion-inicio", "/api/admin/configuracion-inicio/**",
            "/api/admin/metodos-pago", "/api/admin/metodos-pago/**",
            "/api/admin/ventas", "/api/admin/ventas/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {}) // CORS se configura en WebConfig
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/admin/login").permitAll()
                        .requestMatchers(PUBLIC_PATHS).permitAll()
                        .requestMatchers(ADMIN_PATHS).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
