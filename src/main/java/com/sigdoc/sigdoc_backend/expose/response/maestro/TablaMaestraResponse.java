package com.sigdoc.sigdoc_backend.expose.response.maestro;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sigdoc.sigdoc_backend.entity.maestro.TablaMaestraEntity;
import lombok.*;

import java.util.function.Function;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TablaMaestraResponse {

    private Long tablaMaestraId;
    private String codigoTabla;
    private String codigoItem;
    private String codigo;
    private Integer orden;
    private String abreviatura;
    private String denominacion;
    private Boolean esSistema;
    private Integer estado;

    public static final Function<TablaMaestraEntity, TablaMaestraResponse> toResponse = entity -> TablaMaestraResponse.builder()
            .tablaMaestraId(entity.getTablaMaestraId())
            .codigoTabla(entity.getCodigoTabla())
            .codigoItem(entity.getCodigoItem())
            .codigo(entity.getCodigo())
            .orden(entity.getOrden())
            .abreviatura(entity.getAbreviatura())
            .denominacion(entity.getDenominacion())
            .esSistema(entity.getEsSistema())
            .estado(entity.getEstado())
            .build();

}
