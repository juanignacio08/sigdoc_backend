package com.sigdoc.sigdoc_backend.expose.response.maestro;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sigdoc.sigdoc_backend.entity.maestro.DepartamentoAcademicoEntity;
import lombok.*;

import java.util.function.Function;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepartamentoAcademicoResponse {

    private Long departamentoAcademicoId;
    private String denominacion;
    private String abreviatura;
    private FacultadResponse facultad;
    private Integer estado;

    public static final Function<DepartamentoAcademicoEntity, DepartamentoAcademicoResponse> toResponse =
            entity -> DepartamentoAcademicoResponse.builder()
                    .departamentoAcademicoId(entity.getDepartamentoAcademicoId())
                    .denominacion(entity.getDenominacion())
                    .abreviatura(entity.getAbreviatura())
                    .facultad(FacultadResponse.builder()
                            .facultadId(entity.getFacultad().getFacultadId())
                            .build())
                    .estado(entity.getEstado())
                    .build();

    public static final Function<DepartamentoAcademicoEntity, DepartamentoAcademicoResponse> toResponseDetail =
            entity -> DepartamentoAcademicoResponse.builder()
                    .departamentoAcademicoId(entity.getDepartamentoAcademicoId())
                    .denominacion(entity.getDenominacion())
                    .abreviatura(entity.getAbreviatura())
                    .facultad(FacultadResponse.toResponse.apply(entity.getFacultad()))
                    .estado(entity.getEstado())
                    .build();
}
