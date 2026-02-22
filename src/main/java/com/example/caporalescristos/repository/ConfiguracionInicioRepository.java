package com.example.caporalescristos.repository;

import com.example.caporalescristos.entity.ConfiguracionInicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfiguracionInicioRepository extends JpaRepository<ConfiguracionInicio, Long> {

    /** Obtiene la única fila de configuración (id = 1 por defecto tras inicialización). */
    Optional<ConfiguracionInicio> findFirstByOrderByIdAsc();
}
