package com.sigdoc.sigdoc_backend.expose.request.maestro;

import com.sigdoc.sigdoc_backend.entity.maestro.DepartamentoAcademicoEntity;
import com.sigdoc.sigdoc_backend.expose.util.SecurityUtil;
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
public class DepartamentoAcademicoRequest {

    @NotNull(message = "El id es requerido")
    @Min(value = 1, message = "El id debe ser mayor o igual a 1")
    private Long departamentoAcademicoId;

    @NotBlank(message = "La denominación es requerida")
    @Size(max = 200, message = "La denominación debe tener como máximo 200 caracteres")
    private String denominacion;

    @Size(max = 10, message = "La abreviatura debe tener como máximo 10 caracteres")
    private String abreviatura;

    @NotNull(message = "El id de la facultad es requerido")
    @Min(value = 1, message = "El id de la facultad debe ser mayor o igual a 1")
    private Long facultadId;

    private Integer estado;

    public static final Function<DepartamentoAcademicoRequest, DepartamentoAcademicoEntity> toEntity =
            request -> DepartamentoAcademicoEntity.builder()
                    .denominacion(request.getDenominacion())
                    .abreviatura(request.getAbreviatura())
                    .estado(1)
                    .actualizadoPor(SecurityUtil.getCurrentUserId())
                    .fechaActualizacion(LocalDateTime.now())
                    .creadoPor(SecurityUtil.getCurrentUserId())
                    .fechaCreacion(LocalDateTime.now())
                    .eliminado(false)
                    .build();

    public static void toUpdateEntity(DepartamentoAcademicoRequest request, DepartamentoAcademicoEntity entity) {
        entity.setDenominacion(request.getDenominacion());
        entity.setAbreviatura(request.getAbreviatura());

        if (request.getEstado() != null) {
            entity.setEstado(request.getEstado());
        }

        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());
    }
}
