package com.example.caporalescristos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
    private String path;
    private List<String> detalles; // validaciones, etc.
}
