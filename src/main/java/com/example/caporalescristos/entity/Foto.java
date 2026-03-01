package com.example.caporalescristos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "fotos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Foto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    /** Ruta en disco (/uploads/...) o null si la imagen está en imagenData (BD). */
    @Column(name = "url_imagen", length = 500)
    private String urlImagen;

    /** Imagen guardada en BD (Caporales Cristos – persiste en Render). */
    @Lob
    @Column(name = "imagen_data")
    private byte[] imagenData;

    @Column(name = "content_type_imagen", length = 100)
    private String contentTypeImagen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seccion_id")
    private GaleriaSeccion seccion;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}
