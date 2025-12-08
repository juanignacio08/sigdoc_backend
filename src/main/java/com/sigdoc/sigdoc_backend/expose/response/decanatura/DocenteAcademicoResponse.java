package com.sigdoc.sigdoc_backend.expose.response.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteAcademicoEntity;
import com.sigdoc.sigdoc_backend.expose.util.DateConverterUtil;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.function.Function;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@DynamicInsert
@DynamicUpdate
public class DocenteAcademicoResponse {

    private Long docenteAcademicoId;
    private DocenteResponse docente;
    private String codGradoAcademico;
    private String tituloProfesional;
    private String institucionEgreso;
    private String fechaObtencion;
    private String especialidad;
    private DocumentoResponse documento;
    private Integer estado;

    public static final Function<DocenteAcademicoEntity, DocenteAcademicoResponse> toResponse = entity -> DocenteAcademicoResponse.builder()
            .docenteAcademicoId(entity.getDocenteAcademicoId())
            .docente(DocenteResponse.builder()
                    .docenteId(entity.getDocente().getDocenteId())
                    .build())
            .codGradoAcademico(entity.getCodGradoAcademico())
            .tituloProfesional(entity.getTituloProfesional())
            .institucionEgreso(entity.getInstitucionEgreso())
            .fechaObtencion(DateConverterUtil.formatLocalDateToDDMMYYYY(entity.getFechaObtencion()))
            .especialidad(entity.getEspecialidad())
            .documento(DocumentoResponse.builder()
                    .documentoId(entity.getDocumento().getDocumentoId())
                    .build())
            .estado(entity.getEstado())
            .build();

    public static final Function<DocenteAcademicoEntity, DocenteAcademicoResponse> toResponseDetail = entity -> DocenteAcademicoResponse.builder()
            .docenteAcademicoId(entity.getDocenteAcademicoId())
            .docente(DocenteResponse.toResponse.apply(entity.getDocente()))
            .codGradoAcademico(entity.getCodGradoAcademico())
            .tituloProfesional(entity.getTituloProfesional())
            .institucionEgreso(entity.getInstitucionEgreso())
            .fechaObtencion(DateConverterUtil.formatLocalDateToDDMMYYYY(entity.getFechaObtencion()))
            .especialidad(entity.getEspecialidad())
            .documento(DocumentoResponse.toResponse.apply(entity.getDocumento()))
            .estado(entity.getEstado())
            .build();

}
