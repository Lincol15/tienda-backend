package com.example.caporalescristos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VentaDto {

    private Long id;
    private LocalDateTime fecha;
    private String estado;
    private String clienteNombre;
    private String clienteTelefono;
    private BigDecimal total;
    private String metodoPago;
    private String notas;
    private LocalDateTime fechaCreacion;
    private List<DetalleVentaDto> detalles;
}
