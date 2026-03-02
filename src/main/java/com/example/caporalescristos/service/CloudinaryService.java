package com.example.caporalescristos.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Servicio centralizado para subir imágenes y videos a Cloudinary.
 * Devuelve solo la URL (secure_url) para guardar en BD.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;

    /**
     * Sube una imagen a Cloudinary.
     * @param file archivo; si null o vacío retorna null
     * @param folder carpeta en Cloudinary, ej. "caporales/portada", "c-origen/productos"
     * @return secure_url o null
     */
    public String uploadImage(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            File tempFile = multipartToFile(file);
            Map<String, Object> options = ObjectUtils.asMap(
                    "resource_type", "image",
                    "folder", folder
            );
            @SuppressWarnings("unchecked")
            Map<String, Object> result = cloudinary.uploader().upload(tempFile, options);
            deleteTempFile(tempFile);
            return (String) result.get("secure_url");
        } catch (Exception e) {
            log.error("Error subiendo imagen a Cloudinary: {}", e.getMessage());
            throw new RuntimeException("Error al subir imagen: " + e.getMessage(), e);
        }
    }

    /**
     * Sube un video a Cloudinary.
     * @param file archivo; si null o vacío retorna null
     * @param folder carpeta, ej. "caporales/galeria/videos"
     * @return secure_url o null
     */
    public String uploadVideo(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            File tempFile = multipartToFile(file);
            Map<String, Object> options = ObjectUtils.asMap(
                    "resource_type", "video",
                    "folder", folder
            );
            @SuppressWarnings("unchecked")
            Map<String, Object> result = cloudinary.uploader().upload(tempFile, options);
            deleteTempFile(tempFile);
            return (String) result.get("secure_url");
        } catch (Exception e) {
            log.error("Error subiendo video a Cloudinary: {}", e.getMessage());
            throw new RuntimeException("Error al subir video: " + e.getMessage(), e);
        }
    }

    private static File multipartToFile(MultipartFile multipart) throws IOException {
        File temp = File.createTempFile("cloudinary_", "_" + (multipart.getOriginalFilename() != null ? multipart.getOriginalFilename() : "file"));
        try (FileOutputStream fos = new FileOutputStream(temp)) {
            fos.write(multipart.getBytes());
        }
        return temp;
    }

    private static void deleteTempFile(File file) {
        if (file != null && file.exists()) {
            try {
                file.delete();
            } catch (Exception ignored) {
            }
        }
    }
}
