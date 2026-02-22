package com.example.caporalescristos.repository;

import com.example.caporalescristos.entity.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Long> {

    List<MetodoPago> findByActivoTrueOrderByOrdenAsc();

    List<MetodoPago> findAllByOrderByOrdenAsc();
}
