package com.example.caporalescristos.repository;

import com.example.caporalescristos.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    @Query("SELECT v FROM Venta v LEFT JOIN FETCH v.detalles d LEFT JOIN FETCH d.producto ORDER BY v.fechaCreacion DESC")
    List<Venta> findAllWithDetalles();

    @Query("SELECT v FROM Venta v LEFT JOIN FETCH v.detalles d LEFT JOIN FETCH d.producto WHERE v.id = :id")
    Optional<Venta> findByIdWithDetalles(Long id);
}
