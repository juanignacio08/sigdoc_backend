package com.sigdoc.sigdoc_backend.expose.request.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.ExperienciaProfesionalEntity;
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
public class ExperienciaProfesionalRequest {

    @NotNull(message = "El id es requerido")
    @Min(value = 1, message = "El id debe ser mayor o igual a 1")
    private Long experienciaProfesionalId;

    @NotNull(message = "El campo docenteId es requerido")
    @Min(value = 1, message = "El docenteId debe ser mayor o igual a 1")
    private Long docenteId;

    @Size(max = 200, message = "El campo empresa debe tener como máximo 200 caracteres")
    private String empresa;

    @Size(max = 200, message = "El campo cargo debe tener como máximo 200 caracteres")
    private String cargo;

    private String descripcion;

    private String fechaInicio;

    private String fechaFin;

    @Valid
    private DocumentoRequest documento;

    private Integer estado;

    public static final Function<ExperienciaProfesionalRequest, ExperienciaProfesionalEntity> toEntity =
            request -> ExperienciaProfesionalEntity.builder()
                    .empresa(request.getEmpresa())
                    .cargo(request.getCargo())
                    .descripcion(request.getDescripcion())
                    .fechaInicio(DateConverterUtil.parseFechaDDMMYYYY(request.getFechaInicio()))
                    .fechaFin(DateConverterUtil.parseFechaDDMMYYYY(request.getFechaFin()))
                    .estado(1)
                    .actualizadoPor(SecurityUtil.getCurrentUserId())
                    .fechaActualizacion(LocalDateTime.now())
                    .creadoPor(SecurityUtil.getCurrentUserId())
                    .fechaCreacion(LocalDateTime.now())
                    .eliminado(false)
                    .build();

    public static void toUpdateEntity(ExperienciaProfesionalRequest request, ExperienciaProfesionalEntity entity) {
        entity.setEmpresa(request.getEmpresa());
        entity.setCargo(request.getCargo());
        entity.setDescripcion(request.getDescripcion());
        entity.setFechaInicio(DateConverterUtil.parseFechaDDMMYYYY(request.getFechaInicio()));
        entity.setFechaFin(DateConverterUtil.parseFechaDDMMYYYY(request.getFechaFin()));

        if (request.getEstado() != null) {
            entity.setEstado(request.getEstado());
        }

        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());
    }
}
