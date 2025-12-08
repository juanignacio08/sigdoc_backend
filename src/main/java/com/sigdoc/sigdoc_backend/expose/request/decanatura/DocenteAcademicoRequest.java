package com.sigdoc.sigdoc_backend.expose.request.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteAcademicoEntity;
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
public class DocenteAcademicoRequest {

    @NotNull(message = "El id es requerido")
    @Min(value = 1, message = "El id debe ser mayor o igual a 1")
    private Long docenteAcademicoId;

    @NotNull(message = "El campo docenteId es requerido")
    @Min(value = 1, message = "El docenteId debe ser mayor o igual a 1")
    private Long docenteId;

    @Size(min = 6, max = 6, message = "El código del grado academico debe tener exactamente 6 caracteres")
    private String codGradoAcademico;

    @Size(max = 200, message = "El campo tituloProfesional debe tener como máximo 200 caracteres")
    private String tituloProfesional;

    @Size(max = 200, message = "El campo institucionEgreso debe tener como máximo 200 caracteres")
    private String institucionEgreso;

    private String fechaObtencion;

    @Size(max = 200, message = "El campo especialidad debe tener como máximo 200 caracteres")
    private String especialidad;

    @Valid
    private DocumentoRequest documento;

    private Integer estado;

    public static final Function<DocenteAcademicoRequest, DocenteAcademicoEntity> toEntity =
            request -> DocenteAcademicoEntity.builder()
                    .codGradoAcademico(request.getCodGradoAcademico())
                    .tituloProfesional(request.getTituloProfesional())
                    .institucionEgreso(request.getInstitucionEgreso())
                    .fechaObtencion(DateConverterUtil.parseFechaDDMMYYYY(request.getFechaObtencion()))
                    .especialidad(request.getEspecialidad())
                    .estado(1)
                    .actualizadoPor(SecurityUtil.getCurrentUserId())
                    .fechaActualizacion(LocalDateTime.now())
                    .creadoPor(SecurityUtil.getCurrentUserId())
                    .fechaCreacion(LocalDateTime.now())
                    .eliminado(false)
                    .build();

    public static void toUpdateEntity(DocenteAcademicoRequest request, DocenteAcademicoEntity entity) {
        entity.setCodGradoAcademico(request.getCodGradoAcademico());
        entity.setTituloProfesional(request.getTituloProfesional());
        entity.setInstitucionEgreso(request.getInstitucionEgreso());
        entity.setFechaObtencion(DateConverterUtil.parseFechaDDMMYYYY(request.getFechaObtencion()));
        entity.setEspecialidad(request.getEspecialidad());

        if (request.getEstado() != null) entity.setEstado(request.getEstado());

        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());
    }
}
