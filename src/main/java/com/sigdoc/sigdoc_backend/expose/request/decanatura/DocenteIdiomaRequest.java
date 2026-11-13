package com.sigdoc.sigdoc_backend.expose.request.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteIdiomaEntity;
import com.sigdoc.sigdoc_backend.expose.util.DateConverterUtil;
import com.sigdoc.sigdoc_backend.expose.util.SecurityUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.function.Function;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DocenteIdiomaRequest {

    @NotNull(message = "El id es requerido")
    @Min(value = 1, message = "El id debe ser mayor o igual a 1")
    private Long docenteIdiomaId;

    @NotNull(message = "El campo docenteId es requerido")
    @Min(value = 1, message = "El docenteId debe ser mayor o igual a 1")
    private Long docenteId;

    @NotBlank(message = "El idioma es requerido")
    @Size(min = 6, max = 6, message = "El código del idioma debe tener exactamente 6 caracteres")
    private String codIdioma;

    @NotBlank(message = "El nivel del idioma es requerido")
    @Size(min = 6, max = 6, message = "El campo nivel debe tener exactamente 6 caracteres")
    private String nivel;

    @NotBlank(message = "La institucion de egreso es requrido")
    @Size(max = 200, message = "El campo institución debe tener como máximo 200 caracteres")
    private String institucion;

    private String fechaObtencion;

    @Valid
    private DocumentoRequest documento;

    private Integer estado;

    public static final Function<DocenteIdiomaRequest, DocenteIdiomaEntity> toEntity =
            request -> DocenteIdiomaEntity.builder()
                    .codIdioma(request.getCodIdioma())
                    .nivel(request.getNivel())
                    .institucion(request.getInstitucion())
                    .fechaObtencion(DateConverterUtil.parseFechaDDMMYYYY(request.getFechaObtencion()))
                    .estado(1)
                    .actualizadoPor(SecurityUtil.getCurrentUserId())
                    .fechaActualizacion(LocalDateTime.now())
                    .creadoPor(SecurityUtil.getCurrentUserId())
                    .fechaCreacion(LocalDateTime.now())
                    .eliminado(false)
                    .build();

    public static void toUpdateEntity(DocenteIdiomaRequest request, DocenteIdiomaEntity entity) {
        entity.setCodIdioma(request.getCodIdioma());
        entity.setNivel(request.getNivel());
        entity.setInstitucion(request.getInstitucion());
        entity.setFechaObtencion(DateConverterUtil.parseFechaDDMMYYYY(request.getFechaObtencion()));

        if (request.getEstado() != null)
            entity.setEstado(request.getEstado());

        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());
    }
}
