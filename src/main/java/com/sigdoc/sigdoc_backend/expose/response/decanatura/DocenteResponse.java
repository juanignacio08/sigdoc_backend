package com.sigdoc.sigdoc_backend.expose.response.decanatura;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteEntity;
import com.sigdoc.sigdoc_backend.expose.response.maestro.UbigeoResponse;
import com.sigdoc.sigdoc_backend.expose.util.DateConverterUtil;
import lombok.*;

import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocenteResponse {

    private Long docenteId;
    private String codTipoDocumento;
    private String numDocumento;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String fechaNacimiento;
    private String emailPersonal;
    private String emailInstitucional;
    private String codGenero;
    private String direccion;
    private UbigeoResponse codUbigeoActual;
    private UbigeoResponse codUbigeoProcedencia;
    private String codEstadoCivil;
    private String telefono;
    private String fotografia;
    private Integer estado;

    private List<DocenteAcademicoResponse> infoAcademica;

    public static final Function<DocenteEntity, DocenteResponse> toResponse = entity -> DocenteResponse.builder()
            .docenteId(entity.getDocenteId())
            .codTipoDocumento(entity.getCodTipoDocumento())
            .numDocumento(entity.getNumDocumento())
            .nombre(entity.getNombre())
            .apellidoPaterno(entity.getApellidoPaterno())
            .apellidoMaterno(entity.getApellidoMaterno())
            .fechaNacimiento(DateConverterUtil.formatLocalDateToDDMMYYYY(entity.getFechaNacimiento()))
            .emailPersonal(entity.getEmailPersonal())
            .emailInstitucional(entity.getEmailInstitucional())
            .codGenero(entity.getCodGenero())
            .direccion(entity.getDireccion())
            .codUbigeoActual(UbigeoResponse.builder()
                    .codigoUbigeo(entity.getCodigoUbigeoActual())
                    .build())
            .codUbigeoProcedencia(UbigeoResponse.builder()
                    .codigoUbigeo(entity.getCodUbigeoProcedencia())
                    .build())
            .codEstadoCivil(entity.getCodEstadoCivil())
            .telefono(entity.getTelefono())
            .fotografia(entity.getFotografia())
            .estado(entity.getEstado())
            .build();

    public static final Function<DocenteEntity, DocenteResponse> toResponseDetail = entity -> DocenteResponse.builder()
            .docenteId(entity.getDocenteId())
            .codTipoDocumento(entity.getCodTipoDocumento())
            .numDocumento(entity.getNumDocumento())
            .nombre(entity.getNombre())
            .apellidoPaterno(entity.getApellidoPaterno())
            .apellidoMaterno(entity.getApellidoMaterno())
            .fechaNacimiento(DateConverterUtil.formatLocalDateToDDMMYYYY(entity.getFechaNacimiento()))
            .emailPersonal(entity.getEmailPersonal())
            .emailInstitucional(entity.getEmailInstitucional())
            .codGenero(entity.getCodGenero())
            .direccion(entity.getDireccion())
            .codUbigeoActual(UbigeoResponse.builder()
                    .codigoUbigeo(entity.getCodigoUbigeoActual())
                    .build())
            .codUbigeoProcedencia(UbigeoResponse.builder()
                    .codigoUbigeo(entity.getCodUbigeoProcedencia())
                    .build())
            .codEstadoCivil(entity.getCodEstadoCivil())
            .telefono(entity.getTelefono())
            .fotografia(entity.getFotografia())
            .estado(entity.getEstado())
            .infoAcademica(entity.getAcademicsInfo().stream().map(DocenteAcademicoResponse.toResponse).toList())
            .build();

}
