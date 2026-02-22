package com.example.caporalescristos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionInicioDto {

    /** URL relativa al servidor, ej. /uploads/portada/xxx.jpg. Null si no hay imagen. */
    private String portadaUrl;

    /** URL relativa al servidor, ej. /uploads/logo/xxx.png. Null si no hay imagen. */
    private String logoUrl;
}
