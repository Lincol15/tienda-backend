package com.example.caporalescristos.service;

import com.example.caporalescristos.dto.VideoDto;
import com.example.caporalescristos.dto.VideoRequest;
import com.example.caporalescristos.entity.Video;
import com.example.caporalescristos.exception.ResourceNotFoundException;
import com.example.caporalescristos.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {

    private static final String CLOUDINARY_FOLDER = "caporales/galeria/videos";

    private final VideoRepository videoRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional(readOnly = true)
    public List<VideoDto> listarPublicos() {
        return videoRepository.findByActivoTrueOrderByFechaCreacionDesc().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VideoDto obtenerPorId(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video", id));
        if (!video.getActivo()) {
            throw new ResourceNotFoundException("Video", id);
        }
        return toDto(video);
    }

    @Transactional(readOnly = true)
    public List<VideoDto> listarTodosAdmin() {
        return videoRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VideoDto obtenerPorIdAdmin(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video", id));
        return toDto(video);
    }

    @Transactional
    public VideoDto crear(VideoRequest request, MultipartFile archivoVideo) {
        String urlVideo;
        if (archivoVideo != null && !archivoVideo.isEmpty()) {
            urlVideo = cloudinaryService.uploadVideo(archivoVideo, CLOUDINARY_FOLDER);
            if (urlVideo == null) {
                throw new IllegalArgumentException("Error al subir el video");
            }
        } else if (request.getUrlVideo() != null && !request.getUrlVideo().isBlank()) {
            urlVideo = request.getUrlVideo();
        } else {
            throw new IllegalArgumentException("Debe indicar una URL del video o subir un archivo de video");
        }
        Video video = Video.builder()
                .titulo(request.getTitulo())
                .descripcion(request.getDescripcion())
                .urlVideo(urlVideo)
                .activo(request.getActivo() != null ? request.getActivo() : true)
                .build();
        video = videoRepository.save(video);
        return toDto(video);
    }

    @Transactional
    public VideoDto actualizar(Long id, VideoRequest request, MultipartFile archivoVideo) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video", id));
        if (request.getTitulo() != null) video.setTitulo(request.getTitulo());
        if (request.getDescripcion() != null) video.setDescripcion(request.getDescripcion());
        if (request.getActivo() != null) video.setActivo(request.getActivo());
        if (archivoVideo != null && !archivoVideo.isEmpty()) {
            String nuevaUrl = cloudinaryService.uploadVideo(archivoVideo, CLOUDINARY_FOLDER);
            if (nuevaUrl != null) {
                video.setUrlVideo(nuevaUrl);
            }
        } else if (request.getUrlVideo() != null && !request.getUrlVideo().isBlank()) {
            video.setUrlVideo(request.getUrlVideo());
        }
        video = videoRepository.save(video);
        return toDto(video);
    }

    @Transactional
    public void eliminar(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video", id));
        videoRepository.delete(video);
    }

    private VideoDto toDto(Video v) {
        String url = v.getUrlVideo();
        String tipo = detectarTipoVideo(url);
        String embedUrl = construirEmbedUrl(url, tipo);
        return VideoDto.builder()
                .id(v.getId())
                .titulo(v.getTitulo())
                .descripcion(v.getDescripcion())
                .urlVideo(url)
                .tipoVideo(tipo)
                .embedUrl(embedUrl)
                .activo(v.getActivo())
                .fechaCreacion(v.getFechaCreacion())
                .build();
    }

    private static final Pattern YOUTUBE_ID = Pattern.compile("(?:youtube\\.com/watch\\?v=|youtu\\.be/)([a-zA-Z0-9_-]{11})");

    private String detectarTipoVideo(String url) {
        if (url == null || url.isBlank()) return "DIRECTO";
        String u = url.toLowerCase();
        if (u.contains("youtube.com") || u.contains("youtu.be")) return "YOUTUBE";
        if (u.contains("facebook.com") || u.contains("fb.watch") || u.contains("fb.com")) return "FACEBOOK";
        return "DIRECTO";
    }

    private String construirEmbedUrl(String url, String tipo) {
        if (url == null || url.isBlank()) return null;
        if ("YOUTUBE".equals(tipo)) {
            Matcher m = YOUTUBE_ID.matcher(url);
            if (m.find()) {
                return "https://www.youtube.com/embed/" + m.group(1);
            }
        }
        if ("FACEBOOK".equals(tipo)) {
            return null;
        }
        return url;
    }
}
