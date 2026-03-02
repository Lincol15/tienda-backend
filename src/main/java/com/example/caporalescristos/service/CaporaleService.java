package com.example.caporalescristos.service;

import com.example.caporalescristos.entity.Caporale;
import com.example.caporalescristos.repository.CaporaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Guarda contenido Caporales: foto y video subidos a Cloudinary, solo URLs en BD.
 */
@Service
@RequiredArgsConstructor
public class CaporaleService {

    private static final String FOLDER_FOTO = "caporales/galeria/fotos";
    private static final String FOLDER_VIDEO = "caporales/galeria/videos";

    private final CaporaleRepository caporaleRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public void guardar(String titulo, String descripcion, MultipartFile foto, MultipartFile video) {
        String fotoUrl = cloudinaryService.uploadImage(foto, FOLDER_FOTO);
        String videoUrl = cloudinaryService.uploadVideo(video, FOLDER_VIDEO);
        Caporale caporale = Caporale.builder()
                .titulo(titulo != null ? titulo : "")
                .descripcion(descripcion != null ? descripcion : "")
                .fotoUrl(fotoUrl)
                .videoUrl(videoUrl)
                .build();
        caporaleRepository.save(caporale);
    }
}
