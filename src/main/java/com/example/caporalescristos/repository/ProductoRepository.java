package com.example.caporalescristos.repository;

import com.example.caporalescristos.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByActivoTrueOrderByFechaCreacionDesc();

    List<Producto> findByActivoTrueAndCategoriaIdOrderByFechaCreacionDesc(Long categoriaId);
}
