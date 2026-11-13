package com.sigdoc.sigdoc_backend.expose.response.maestro;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sigdoc.sigdoc_backend.entity.maestro.FacultadEntity;
import lombok.*;

import java.util.function.Function;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacultadResponse {

    private Long facultadId;
    private String denominacion;
    private String abreviatura;
    private Integer estado;

    public static final Function<FacultadEntity, FacultadResponse> toResponse =
            entity -> FacultadResponse.builder()
                    .facultadId(entity.getFacultadId())
                    .denominacion(entity.getDenominacion())
                    .abreviatura(entity.getAbreviatura())
                    .estado(entity.getEstado())
                    .build();

}
