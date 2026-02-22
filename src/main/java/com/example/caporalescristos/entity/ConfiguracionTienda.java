package com.example.caporalescristos.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Configuración de la tienda C'Origen (singleton).
 * Número de WhatsApp al que llegan los mensajes de compra.
 */
@Entity
@Table(name = "configuracion_tienda")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfiguracionTienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "whatsapp_numero", length = 20)
    private String whatsappNumero;
}
