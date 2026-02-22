package com.example.caporalescristos.dto;

import lombok.Data;

import java.util.List;

@Data
public class VentaRequest {

    private String clienteNombre;
    private String clienteTelefono;
    private String estado;
    private String metodoPago;
    private String notas;
    private List<DetalleVentaRequest> items;
}
