package com.sigdoc.sigdoc_backend.expose.request.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteLaboralEntity;
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
public class DocenteLaboralRequest {

    @NotNull(message = "El id es requerido")
    @Min(value = 1, message = "El id debe ser mayor o igual a 1")
    private Long docenteLaboralId;

    @NotNull(message = "El campo docenteId es requerido")
    @Min(value = 1, message = "El docenteId debe ser mayor o igual a 1")
    private Long docenteId;

    @NotNull(message = "El campo departamentoId es requerido")
    @Min(value = 1, message = "El departamentoId debe ser mayor o igual a 1")
    private Long departamentoId;

    @Size(max = 10, message = "El código institucional debe tener como máximo 10 caracteres")
    private String codigoInstitucional;

    @Email(message = "El email institucional no tiene un formato válido")
    @Size(max = 120, message = "El email institucional debe tener como máximo 120 caracteres")
    private String emailInstitucional;

    @NotBlank(message = "El regimen laboral es requerido.")
    @Size(min = 6, max = 6, message = "El código de régimen laboral debe tener exactamente 6 caracteres")
    private String codRegimenLaboral;

    @Size(min = 6, max = 6, message = "El código de categoría de docente nombrado debe tener exactamente 6 caracteres")
    private String codCategoriaDocenteNombrado;

    @Size(min = 6, max = 6, message = "El código de nivel de docente contratado debe tener exactamente 6 caracteres")
    private String codNivelDocenteContratado;

    @Size(min = 6, max = 6, message = "El código de tiempo de dedicación debe tener exactamente 6 caracteres")
    private String codTiempoDedicacion;

    private String fechaInicio;
    private String fechaFin;

    @Valid
    private DocumentoRequest documento;

    private Integer estado;

    public static final Function<DocenteLaboralRequest, DocenteLaboralEntity> toEntity =
            request -> DocenteLaboralEntity.builder()
                    .codigoInstitucional(request.getCodigoInstitucional())
                    .emailInstitucional(request.getEmailInstitucional())
                    .codRegimenLaboral(request.getCodRegimenLaboral())
                    .codCategoriaDocenteNombrado(request.getCodCategoriaDocenteNombrado())
                    .codNivelDocenteContratado(request.getCodNivelDocenteContratado())
                    .codTiempoDedicacion(request.getCodTiempoDedicacion())
                    .fechaInicio(DateConverterUtil.parseFechaDDMMYYYY(request.getFechaInicio()))
                    .fechaFin(DateConverterUtil.parseFechaDDMMYYYY(request.getFechaFin()))
                    .estado(1)
                    .actualizadoPor(SecurityUtil.getCurrentUserId())
                    .fechaActualizacion(LocalDateTime.now())
                    .creadoPor(SecurityUtil.getCurrentUserId())
                    .fechaCreacion(LocalDateTime.now())
                    .eliminado(false)
                    .build();

    public static void toUpdateEntity(DocenteLaboralRequest request, DocenteLaboralEntity entity) {
        entity.setCodigoInstitucional(request.getCodigoInstitucional());
        entity.setEmailInstitucional(request.getEmailInstitucional());
        entity.setCodRegimenLaboral(request.getCodRegimenLaboral());
        entity.setCodCategoriaDocenteNombrado(request.getCodCategoriaDocenteNombrado());
        entity.setCodNivelDocenteContratado(request.getCodNivelDocenteContratado());
        entity.setCodTiempoDedicacion(request.getCodTiempoDedicacion());
        entity.setFechaInicio(DateConverterUtil.parseFechaDDMMYYYY(request.getFechaInicio()));
        entity.setFechaFin(DateConverterUtil.parseFechaDDMMYYYY(request.getFechaFin()));

        if (request.getEstado() != null) {
            entity.setEstado(request.getEstado());
        }

        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());
    }
}
