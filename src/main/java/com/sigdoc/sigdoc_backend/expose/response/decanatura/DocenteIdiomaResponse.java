package com.sigdoc.sigdoc_backend.expose.response.decanatura;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteIdiomaEntity;
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
public class DocenteIdiomaResponse {

    private Long docenteIdiomaId;
    private DocenteResponse docente;
    private String codIdioma;
    private String nivel;
    private String institucion;
    private String fechaObtencion;
    private DocumentoResponse documento;
    private Integer estado;

    public static final Function<DocenteIdiomaEntity, DocenteIdiomaResponse> toResponse =
            entity -> DocenteIdiomaResponse.builder()
                    .docenteIdiomaId(entity.getDocenteIdiomaId())
                    .docente(DocenteResponse.builder()
                            .docenteId(entity.getDocente().getDocenteId())
                            .build())
                    .codIdioma(entity.getCodIdioma())
                    .nivel(entity.getNivel())
                    .institucion(entity.getInstitucion())
                    .fechaObtencion(DateConverterUtil.formatLocalDateToDDMMYYYY(entity.getFechaObtencion()))
                    .documento(entity.getDocumento() != null ? DocumentoResponse.builder()
                            .documentoId(entity.getDocumento().getDocumentoId())
                            .build() : null)
                    .estado(entity.getEstado())
                    .build();

    public static final Function<DocenteIdiomaEntity, DocenteIdiomaResponse> toResponseDetail =
            entity -> DocenteIdiomaResponse.builder()
                    .docenteIdiomaId(entity.getDocenteIdiomaId())
                    .docente(DocenteResponse.toResponse.apply(entity.getDocente()))
                    .codIdioma(entity.getCodIdioma())
                    .nivel(entity.getNivel())
                    .institucion(entity.getInstitucion())
                    .fechaObtencion(DateConverterUtil.formatLocalDateToDDMMYYYY(entity.getFechaObtencion()))
                    .documento(entity.getDocumento() != null ?
                            DocumentoResponse.toResponse.apply(entity.getDocumento()) : null)
                    .estado(entity.getEstado())
                    .build();
}
