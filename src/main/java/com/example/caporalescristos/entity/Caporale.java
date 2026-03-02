package com.example.caporalescristos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad para contenido Caporales: título, descripción, foto y video.
 * URLs de Cloudinary (secure_url) guardadas en BD.
 */
@Entity
@Table(name = "caporales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Caporale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    @Column(name = "video_url", length = 500)
    private String videoUrl;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}
