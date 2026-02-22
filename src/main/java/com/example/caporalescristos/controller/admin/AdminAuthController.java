package com.example.caporalescristos.controller.admin;

import com.example.caporalescristos.dto.LoginRequest;
import com.example.caporalescristos.dto.LoginResponse;
import com.example.caporalescristos.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Login del admin. Ruta pública (sin JWT) para obtener el token.
 * El resto de /api/admin/** requiere Bearer token.
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
