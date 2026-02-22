package com.example.caporalescristos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Venta o pedido de la tienda C'Origen.
 * Se relaciona con DetalleVenta (líneas de la venta, cada una con producto + cantidad + precio).
 */
@Entity
@Table(name = "ventas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String estado = "PENDIENTE";

    @Column(name = "cliente_nombre", length = 200)
    private String clienteNombre;

    @Column(name = "cliente_telefono", length = 30)
    private String clienteTelefono;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(name = "metodo_pago", length = 50)
    private String metodoPago;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DetalleVenta> detalles = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (fecha == null) fecha = LocalDateTime.now();
    }
}
