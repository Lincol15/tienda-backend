package com.example.caporalescristos.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DetalleVentaRequest {

    private Long productoId;
    private Integer cantidad;
    private BigDecimal precioUnitario;
}
