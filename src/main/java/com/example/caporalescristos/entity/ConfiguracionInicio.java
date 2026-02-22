package com.example.caporalescristos.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Configuración de la página de inicio (portada/banner y logo).
 * Una sola fila; el admin actualiza las URLs al subir nuevas imágenes.
 */
@Entity
@Table(name = "configuracion_inicio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfiguracionInicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "portada_url", length = 500)
    private String portadaUrl;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;
}
