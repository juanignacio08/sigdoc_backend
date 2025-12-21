package com.sigdoc.sigdoc_backend.expose.response.decanatura;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sigdoc.sigdoc_backend.entity.decanatura.CapacitacionEntity;
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
public class CapacitacionResponse {

    private Long capacitacionId;
    private DocenteResponse docente;
    private String titulo;
    private String institucion;
    private Integer horas;
    private String fechaInicio;
    private String fechaFin;
    private Double creditos;
    private DocumentoResponse documento;
    private Integer estado;

    public static final Function<CapacitacionEntity, CapacitacionResponse> toResponse =
            entity -> CapacitacionResponse.builder()
                    .capacitacionId(entity.getCapacitacionId())
                    .docente(DocenteResponse.builder()
                            .docenteId(entity.getDocente().getDocenteId())
                            .build())
                    .titulo(entity.getTitulo())
                    .institucion(entity.getInstitucion())
                    .horas(entity.getHoras())
                    .fechaInicio(DateConverterUtil.formatLocalDateToDDMMYYYY(entity.getFechaInicio()))
                    .fechaFin(DateConverterUtil.formatLocalDateToDDMMYYYY(entity.getFechaFin()))
                    .creditos(entity.getCreditos() != null ? entity.getCreditos().doubleValue() : null)
                    .documento(entity.getDocumento() != null
                            ? DocumentoResponse.builder()
                            .documentoId(entity.getDocumento().getDocumentoId())
                            .build()
                            : null)
                    .estado(entity.getEstado())
                    .build();

    public static final Function<CapacitacionEntity, CapacitacionResponse> toResponseDetail =
            entity -> CapacitacionResponse.builder()
                    .capacitacionId(entity.getCapacitacionId())
                    .docente(DocenteResponse.toResponse.apply(entity.getDocente()))
                    .titulo(entity.getTitulo())
                    .institucion(entity.getInstitucion())
                    .horas(entity.getHoras())
                    .fechaInicio(DateConverterUtil.formatLocalDateToDDMMYYYY(entity.getFechaInicio()))
                    .fechaFin(DateConverterUtil.formatLocalDateToDDMMYYYY(entity.getFechaFin()))
                    .creditos(entity.getCreditos() != null ? entity.getCreditos().doubleValue() : null)
                    .documento(entity.getDocumento() != null
                            ? DocumentoResponse.toResponse.apply(entity.getDocumento())
                            : null)
                    .estado(entity.getEstado())
                    .build();
}
