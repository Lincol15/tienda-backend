package com.example.caporalescristos.config;

import com.example.caporalescristos.entity.ConfiguracionInicio;
import com.example.caporalescristos.entity.ConfiguracionTienda;
import com.example.caporalescristos.entity.Usuario;
import com.example.caporalescristos.repository.ConfiguracionInicioRepository;
import com.example.caporalescristos.repository.ConfiguracionTiendaRepository;
import com.example.caporalescristos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Crea el usuario admin y la configuración de tienda por defecto si no existen.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final ConfiguracionTiendaRepository configuracionTiendaRepository;
    private final ConfiguracionInicioRepository configuracionInicioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.existsByUsername("albert.torres")) {
            log.info("Usuario admin ya existe.");
        } else {
            Usuario admin = Usuario.builder()
                    .username("albert.torres")
                    .password(passwordEncoder.encode("albert.torres2026"))
                    .rol(Usuario.Rol.ADMIN)
                    .activo(true)
                    .build();
            usuarioRepository.save(admin);
            log.info("Usuario admin creado: albert.torres (cambiar contraseña en producción si aplica)");
        }

        if (configuracionTiendaRepository.count() == 0) {
            configuracionTiendaRepository.save(ConfiguracionTienda.builder().whatsappNumero(null).build());
            log.info("Configuración de tienda creada (editar WhatsApp desde el panel admin)");
        }

        if (configuracionInicioRepository.count() == 0) {
            configuracionInicioRepository.save(ConfiguracionInicio.builder().portadaUrl(null).logoUrl(null).build());
            log.info("Configuración de inicio creada (portada y logo desde el panel admin)");
        }
    }
}
