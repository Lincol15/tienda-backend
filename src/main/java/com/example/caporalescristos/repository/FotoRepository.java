package com.example.caporalescristos.repository;

import com.example.caporalescristos.entity.Foto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FotoRepository extends JpaRepository<Foto, Long> {

    List<Foto> findByActivoTrueOrderByFechaCreacionDesc();

    List<Foto> findBySeccion_IdAndActivoTrueOrderByFechaCreacionDesc(Long seccionId);

    List<Foto> findBySeccion_Id(Long seccionId);
}
