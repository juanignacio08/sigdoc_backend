package com.sigdoc.sigdoc_backend.expose.request.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.CapacitacionEntity;
import com.sigdoc.sigdoc_backend.expose.util.DateConverterUtil;
import com.sigdoc.sigdoc_backend.expose.util.SecurityUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.function.Function;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CapacitacionRequest {

    @NotNull(message = "El id de la capacitación es requerido")
    @Min(value = 1, message = "El id de la capacitación debe ser mayor o igual a 1")
    private Long capacitacionId;

    @NotNull(message = "El campo docenteId es requerido")
    @Min(value = 1, message = "El docenteId debe ser mayor o igual a 1")
    private Long docenteId;

    @Size(max = 200, message = "El campo título debe tener como máximo 200 caracteres")
    private String titulo;

    @Size(max = 200, message = "El campo institución debe tener como máximo 200 caracteres")
    private String institucion;

    @PositiveOrZero(message = "Las horas deben ser un valor positivo")
    private Integer horas;

    private String fechaInicio;

    private String fechaFin;

    @Digits(integer = 3, fraction = 2, message = "Los créditos deben tener hasta 3 enteros y 2 decimales")
    private BigDecimal creditos;

    @Valid
    private DocumentoRequest documento;

    private Integer estado;

    public static final Function<CapacitacionRequest, CapacitacionEntity> toEntity =
            request -> CapacitacionEntity.builder()
                    .titulo(request.getTitulo())
                    .institucion(request.getInstitucion())
                    .horas(request.getHoras())
                    .fechaInicio(DateConverterUtil.parseFechaDDMMYYYY(request.getFechaInicio()))
                    .fechaFin(DateConverterUtil.parseFechaDDMMYYYY(request.getFechaFin()))
                    .creditos(request.getCreditos())
                    .estado(1)
                    .actualizadoPor(SecurityUtil.getCurrentUserId())
                    .fechaActualizacion(LocalDateTime.now())
                    .creadoPor(SecurityUtil.getCurrentUserId())
                    .fechaCreacion(LocalDateTime.now())
                    .eliminado(false)
                    .build();

    public static void toUpdateEntity(CapacitacionRequest request, CapacitacionEntity entity) {
        entity.setTitulo(request.getTitulo());
        entity.setInstitucion(request.getInstitucion());
        entity.setHoras(request.getHoras());
        entity.setFechaInicio(DateConverterUtil.parseFechaDDMMYYYY(request.getFechaInicio()));
        entity.setFechaFin(DateConverterUtil.parseFechaDDMMYYYY(request.getFechaFin()));
        entity.setCreditos(request.getCreditos());

        if (request.getEstado() != null) {
            entity.setEstado(request.getEstado());
        }

        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());
    }
}
