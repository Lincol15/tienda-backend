package com.example.caporalescristos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDto {

    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String urlImagen;
    private Long categoriaId;
    private String categoriaNombre;
    private Integer stock;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}
