package com.example.caporalescristos.repository;

import com.example.caporalescristos.entity.GaleriaSeccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GaleriaSeccionRepository extends JpaRepository<GaleriaSeccion, Long> {

    List<GaleriaSeccion> findByActivoTrueOrderByOrdenAsc();

    List<GaleriaSeccion> findAllByOrderByOrdenAsc();
}
