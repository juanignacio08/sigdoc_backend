package com.sigdoc.sigdoc_backend.expose.response.decanatura;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteAcademicoEntity;
import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteLaboralEntity;
import com.sigdoc.sigdoc_backend.expose.response.maestro.DepartamentoAcademicoResponse;
import com.sigdoc.sigdoc_backend.expose.response.maestro.TablaMaestraResponse;
import com.sigdoc.sigdoc_backend.expose.util.DateConverterUtil;
import com.sigdoc.sigdoc_backend.service.cache.TablaMaestraCache;
import lombok.*;

import java.util.function.Function;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocenteLaboralResponse {

    private Long docenteLaboralId;
    private DocenteResponse docente;
    private DepartamentoAcademicoResponse departamentoAcademico;
    private String codigoInstitucional;
    private String emailInstitucional;
    private TablaMaestraResponse codRegimenLaboral;
    private TablaMaestraResponse codCategoriaDocenteNombrado;
    private TablaMaestraResponse codNivelDocenteContratado;
    private TablaMaestraResponse codTiempoDedicacion;
    private String fechaInicio;
    private String fechaFin;
    private DocumentoResponse documento;
    private Integer estado;

    public static final Function<DocenteLaboralEntity, DocenteLaboralResponse> toResponse =
            entity -> DocenteLaboralResponse.builder()
                    .docenteLaboralId(entity.getDocenteLaboralId())
                    .docente(DocenteResponse.builder()
                            .docenteId(entity.getDocente().getDocenteId())
                            .build())
                    .departamentoAcademico(DepartamentoAcademicoResponse.builder()
                            .departamentoAcademicoId(entity.getDepartamentoAcademico().getDepartamentoAcademicoId())
                            .build())
                    .codigoInstitucional(entity.getCodigoInstitucional())
                    .emailInstitucional(entity.getEmailInstitucional())
                    .codRegimenLaboral(TablaMaestraResponse.builder()
                            .codigo(entity.getCodRegimenLaboral())
                            .build())
                    .codCategoriaDocenteNombrado(
                            TablaMaestraResponse.builder()
                                    .codigo(entity.getCodCategoriaDocenteNombrado())
                                    .build())
                    .codNivelDocenteContratado(
                            TablaMaestraResponse.builder()
                                    .codigo(entity.getCodNivelDocenteContratado())
                                    .build())
                    .codTiempoDedicacion(
                            TablaMaestraResponse.builder()
                                    .codigo(entity.getCodTiempoDedicacion())
                                    .build())
                    .fechaInicio(DateConverterUtil.formatLocalDateToDDMMYYYY(entity.getFechaInicio()))
                    .fechaFin(DateConverterUtil.formatLocalDateToDDMMYYYY(entity.getFechaFin()))
                    .documento(entity.getDocumento() != null
                            ? DocumentoResponse.builder()
                            .documentoId(entity.getDocumento().getDocumentoId())
                            .build()
                            : null)
                    .estado(entity.getEstado())
                    .build();

    public static final Function<DocenteLaboralEntity, DocenteLaboralResponse> toResponseDetail =
            entity -> DocenteLaboralResponse.builder()
                    .docenteLaboralId(entity.getDocenteLaboralId())
                    .docente(DocenteResponse.toResponse.apply(entity.getDocente()))
                    .departamentoAcademico(DepartamentoAcademicoResponse.toResponse.apply(
                            entity.getDepartamentoAcademico()))
                    .codigoInstitucional(entity.getCodigoInstitucional())
                    .emailInstitucional(entity.getEmailInstitucional())
                    .codRegimenLaboral(TablaMaestraResponse.builder()
                            .codigo(entity.getCodRegimenLaboral())
                            .build())
                    .codCategoriaDocenteNombrado(
                            TablaMaestraResponse.builder()
                                    .codigo(entity.getCodCategoriaDocenteNombrado())
                                    .build())
                    .codNivelDocenteContratado(
                            TablaMaestraResponse.builder()
                                    .codigo(entity.getCodNivelDocenteContratado())
                                    .build())
                    .codTiempoDedicacion(
                            TablaMaestraResponse.builder()
                                    .codigo(entity.getCodTiempoDedicacion())
                                    .build())
                    .fechaInicio(DateConverterUtil.formatLocalDateToDDMMYYYY(entity.getFechaInicio()))
                    .fechaFin(DateConverterUtil.formatLocalDateToDDMMYYYY(entity.getFechaFin()))
                    .documento(entity.getDocumento() != null
                            ? DocumentoResponse.toResponse.apply(entity.getDocumento())
                            : null)
                    .estado(entity.getEstado())
                    .build();

    public static DocenteLaboralResponse toResponseWithMasterTable(
            DocenteLaboralEntity entity,
            TablaMaestraCache cache
    ) {
        return DocenteLaboralResponse.builder()
                .docenteLaboralId(entity.getDocenteLaboralId())
                .docente(DocenteResponse.toResponse.apply(entity.getDocente()))
                .departamentoAcademico(DepartamentoAcademicoResponse.toResponse.apply(
                        entity.getDepartamentoAcademico()))
                .codigoInstitucional(entity.getCodigoInstitucional())
                .emailInstitucional(entity.getEmailInstitucional())
                .codRegimenLaboral(TablaMaestraResponse.buildTablaMaestra(
                        cache.REGIMEN_LABORAL,
                        entity.getCodRegimenLaboral(),
                        cache
                ))
                .codCategoriaDocenteNombrado(TablaMaestraResponse.buildTablaMaestra(
                        cache.CATEGORIA_DOCENTE,
                        entity.getCodCategoriaDocenteNombrado(),
                        cache
                ))
                .codNivelDocenteContratado(TablaMaestraResponse.buildTablaMaestra(
                        cache.NIVEL_DOCENTE,
                        entity.getCodNivelDocenteContratado(),
                        cache
                ))
                .codTiempoDedicacion(TablaMaestraResponse.buildTablaMaestra(
                        cache.TIEMPO_DEDICACION,
                        entity.getCodTiempoDedicacion(),
                        cache
                ))
                .fechaInicio(DateConverterUtil.formatLocalDateToDDMMYYYY(entity.getFechaInicio()))
                .fechaFin(DateConverterUtil.formatLocalDateToDDMMYYYY(entity.getFechaFin()))
                .documento(entity.getDocumento() != null
                        ? DocumentoResponse.toResponse.apply(entity.getDocumento())
                        : null)
                .estado(entity.getEstado())
                .build();
    }

}
