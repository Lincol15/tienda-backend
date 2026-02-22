package com.example.caporalescristos.service;

import com.example.caporalescristos.dto.LoginRequest;
import com.example.caporalescristos.dto.LoginResponse;
import com.example.caporalescristos.security.JwtUtil;
import com.example.caporalescristos.security.UsuarioPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Usuario y contraseña son obligatorios");
        }
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UsuarioPrincipal principal = (UsuarioPrincipal) auth.getPrincipal();
        String token = jwtUtil.generateToken(principal.getUsername(), principal.getAuthorities().iterator().next().getAuthority().replace("ROLE_", ""));
        return LoginResponse.builder()
                .token(token)
                .username(principal.getUsername())
                .rol("ADMIN")
                .expiresIn(jwtUtil.getExpirationSeconds())
                .build();
    }
}
