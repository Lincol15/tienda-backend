package com.example.caporalescristos.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    /** Sube un archivo y devuelve la URL pública (ej: /uploads/fotos/xxx.jpg) */
    public String storeFile(MultipartFile file, String subfolder) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String originalName = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = "";
        int i = originalName.lastIndexOf('.');
        if (i > 0) {
            extension = originalName.substring(i);
        }
        String fileName = UUID.randomUUID().toString() + extension;

        Path targetDir = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(subfolder);
        try {
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }
            Path targetLocation = targetDir.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + subfolder + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar el archivo: " + fileName, e);
        }
    }

    public void deleteFile(String urlPath) {
        if (urlPath == null || !urlPath.startsWith("/uploads/")) {
            return;
        }
        String relativePath = urlPath.replace("/uploads/", "");
        Path path = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(relativePath);
        try {
            Files.deleteIfExists(path);
        } catch (IOException ignored) {
        }
    }
}
