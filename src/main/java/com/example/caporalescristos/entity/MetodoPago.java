package com.example.caporalescristos.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Métodos de pago que el admin puede configurar (Yape, Plin, etc.).
 * WhatsApp usa ConfiguracionTienda.whatsappNumero; aquí se agregan otros como Yape.
 */
@Entity
@Table(name = "metodos_pago")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    /** YAPE, PLIN, OTRO (WhatsApp se toma de ConfiguracionTienda) */
    @Column(nullable = false, length = 50)
    private String tipo;

    /** Número de teléfono, instrucciones o link */
    @Column(length = 500)
    private String valor;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @Column(nullable = false)
    @Builder.Default
    private Integer orden = 0;
}
