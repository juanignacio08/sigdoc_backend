package com.sigdoc.sigdoc_backend.expose.response.decanatura;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sigdoc.sigdoc_backend.entity.decanatura.DocumentoEntity;
import lombok.*;

import java.util.function.Function;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentoResponse {
    private Long documentoId;
    private String denominacion;
    private String codTipoDocumento;
    private String url;
    private DocenteResponse docente;
    private Integer estado;

    public static final Function<DocumentoEntity, DocumentoResponse> toResponse = entity -> DocumentoResponse.builder()
            .documentoId(entity.getDocumentoId())
            .denominacion(entity.getDenominacion())
            .codTipoDocumento(entity.getCodTipoDocumento())
            .url(entity.getUrl())
            .docente(entity.getDocente() != null ?
                    DocenteResponse.builder()
                            .docenteId(entity.getDocente().getDocenteId())
                            .build() : null
            )
            .estado(entity.getEstado())
            .build();

    public static final Function<DocumentoEntity, DocumentoResponse> toResponseDetail = entity -> DocumentoResponse.builder()
            .documentoId(entity.getDocumentoId())
            .denominacion(entity.getDenominacion())
            .codTipoDocumento(entity.getCodTipoDocumento())
            .url(entity.getUrl())
            .docente(entity.getDocente() != null
                    ? DocenteResponse.toResponse.apply(entity.getDocente())
                    : null)
            .estado(entity.getEstado())
            .build();

}
