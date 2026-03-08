package com.sigdoc.sigdoc_backend.expose.response.maestro;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sigdoc.sigdoc_backend.entity.maestro.EscuelaProfesionalEntity;
import lombok.*;

import java.util.function.Function;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EscuelaProfesionalResponse {

    private Long escuelaProfesionalId;
    private String denominacion;
    private String abreviatura;
    private DepartamentoAcademicoResponse departamentoAcademico;
    private String color;
    private String imagen;
    private Integer estado;

    public static final Function<EscuelaProfesionalEntity, EscuelaProfesionalResponse> toResponse =
            entity -> EscuelaProfesionalResponse.builder()
                    .escuelaProfesionalId(entity.getEscuelaProfesionalId())
                    .denominacion(entity.getDenominacion())
                    .abreviatura(entity.getAbreviatura())
                    .departamentoAcademico(DepartamentoAcademicoResponse.builder()
                            .departamentoAcademicoId(entity.getDepartamentoAcademico().getDepartamentoAcademicoId())
                            .build())
                    .color(entity.getColor())
                    .imagen(entity.getImagen())
                    .estado(entity.getEstado())
                    .build();

    public static final Function<EscuelaProfesionalEntity, EscuelaProfesionalResponse> toResponseDetail =
            entity -> EscuelaProfesionalResponse.builder()
                    .escuelaProfesionalId(entity.getEscuelaProfesionalId())
                    .denominacion(entity.getDenominacion())
                    .abreviatura(entity.getAbreviatura())
                    .departamentoAcademico(DepartamentoAcademicoResponse.toResponse.apply(entity.getDepartamentoAcademico()))
                    .color(entity.getColor())
                    .imagen(entity.getImagen())
                    .estado(entity.getEstado())
                    .build();

}
