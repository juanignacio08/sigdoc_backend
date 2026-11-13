package com.sigdoc.sigdoc_backend.expose.response.decanatura;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sigdoc.sigdoc_backend.entity.decanatura.ExperienciaProfesionalEntity;
import com.sigdoc.sigdoc_backend.expose.util.DateConverterUtil;
import lombok.*;

import java.util.function.Function;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExperienciaProfesionalResponse {

    private Long experienciaProfesionalId;
    private DocenteResponse docente;
    private String empresa;
    private String cargo;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;
    private DocumentoResponse documento;
    private Integer estado;

    public static final Function<ExperienciaProfesionalEntity, ExperienciaProfesionalResponse> toResponse =
            entity -> ExperienciaProfesionalResponse.builder()
                    .experienciaProfesionalId(entity.getExperienciaProfesionalId())
                    .docente(DocenteResponse.builder()
                            .docenteId(entity.getDocente().getDocenteId())
                            .build())
                    .empresa(entity.getEmpresa())
                    .cargo(entity.getCargo())
                    .descripcion(entity.getDescripcion())
                    .fechaInicio(DateConverterUtil.formatLocalDateToDDMMYYYY(entity.getFechaInicio()))
                    .fechaFin(DateConverterUtil.formatLocalDateToDDMMYYYY(entity.getFechaFin()))
                    .documento(entity.getDocumento() != null
                            ? DocumentoResponse.builder()
                            .documentoId(entity.getDocumento().getDocumentoId())
                            .build()
                            : null)
                    .estado(entity.getEstado())
                    .build();

    public static final Function<ExperienciaProfesionalEntity, ExperienciaProfesionalResponse> toResponseDetail =
            entity -> ExperienciaProfesionalResponse.builder()
                    .experienciaProfesionalId(entity.getExperienciaProfesionalId())
                    .docente(DocenteResponse.toResponse.apply(entity.getDocente()))
                    .empresa(entity.getEmpresa())
                    .cargo(entity.getCargo())
                    .descripcion(entity.getDescripcion())
                    .fechaInicio(DateConverterUtil.formatLocalDateToDDMMYYYY(entity.getFechaInicio()))
                    .fechaFin(DateConverterUtil.formatLocalDateToDDMMYYYY(entity.getFechaFin()))
                    .documento(entity.getDocumento() != null
                            ? DocumentoResponse.toResponse.apply(entity.getDocumento())
                            : null)
                    .estado(entity.getEstado())
                    .build();

}
