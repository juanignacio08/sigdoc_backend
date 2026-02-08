package com.sigdoc.sigdoc_backend.expose.request.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocumentoEntity;
import com.sigdoc.sigdoc_backend.expose.util.SecurityUtil;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.function.Function;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DocumentoRequest {

    @NotNull(message = "El id es requerido.")
    @Min(value = 1, message = "El id debe de ser mayor o igual a 1")
    private Long documentoId;

    @NotBlank(message = "El campo denominación es requerido")
    @Size(max = 200, message = "La denominación debe tener como máximo 200 caracteres")
    private String denominacion;

    @NotBlank(message = "El código de tipo de documento es requerido")
    @Size(min = 6, max = 6, message = "El código de tipo de documento debe tener exactamente 6 caracteres")
    private String codTipoDocumento;

    private String url;

    @NotNull(message = "El DocenteId es requerido")
    @Min(value = 1, message = "El DocenteId debe ser mayor o igual a 1")
    private Long docenteId;

    private Integer estado;

    public static final Function<DocumentoRequest, DocumentoEntity> toEntity = request -> DocumentoEntity.builder()
            .denominacion(request.getDenominacion())
            .codTipoDocumento(request.getCodTipoDocumento())
            .url(request.getUrl())
            .estado(1)
            .actualizadoPor(SecurityUtil.getCurrentUserId())
            .fechaActualizacion(LocalDateTime.now())
            .creadoPor(SecurityUtil.getCurrentUserId())
            .fechaCreacion(LocalDateTime.now())
            .eliminado(false)
            .build();

    public static void toUpdateEntity(DocumentoRequest request, DocumentoEntity entity) {
        entity.setDenominacion(request.getDenominacion());
        entity.setUrl(request.getUrl());
        entity.setCodTipoDocumento(request.getCodTipoDocumento());

        if (request.getEstado() != null) entity.setEstado(request.getEstado());

        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());
    }

}
