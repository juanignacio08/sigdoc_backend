package com.sigdoc.sigdoc_backend.expose.response.decanatura;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteEntity;
import com.sigdoc.sigdoc_backend.expose.response.maestro.TablaMaestraResponse;
import com.sigdoc.sigdoc_backend.expose.response.maestro.UbigeoResponse;
import com.sigdoc.sigdoc_backend.expose.util.DateConverterUtil;
import com.sigdoc.sigdoc_backend.service.cache.TablaMaestraCache;
import lombok.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Path;
import java.nio.file.Paths;
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
    private TablaMaestraResponse codTipoDocumento;
    private String numDocumento;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String fechaNacimiento;
    private String emailPersonal;
    private String emailInstitucional;
    private TablaMaestraResponse codGenero;
    private String direccion;
    private UbigeoResponse codUbigeoActual;
    private UbigeoResponse codUbigeoProcedencia;
    private TablaMaestraResponse codEstadoCivil;
    private String telefono;
    private String fotografia;
    private String urlFotografia;
    private Integer estado;

    private List<DocenteAcademicoResponse> infoAcademica;

    public static final Function<DocenteEntity, DocenteResponse> toResponse = entity -> DocenteResponse.builder()
            .docenteId(entity.getDocenteId())
            .codTipoDocumento(
                    TablaMaestraResponse.builder()
                            .codigo(entity.getCodTipoDocumento())
                            .build()
            )
            .numDocumento(entity.getNumDocumento())
            .nombre(entity.getNombre())
            .apellidoPaterno(entity.getApellidoPaterno())
            .apellidoMaterno(entity.getApellidoMaterno())
            .fechaNacimiento(DateConverterUtil.formatLocalDateToDDMMYYYY(entity.getFechaNacimiento()))
            .emailPersonal(entity.getEmailPersonal())
            .emailInstitucional(entity.getEmailInstitucional())
            .codGenero(
                    TablaMaestraResponse.builder()
                            .codigo(entity.getCodGenero())
                            .build()
            )
            .direccion(entity.getDireccion())
            .codUbigeoActual(UbigeoResponse.builder()
                    .codigoUbigeo(entity.getCodigoUbigeoActual())
                    .build())
            .codUbigeoProcedencia(UbigeoResponse.builder()
                    .codigoUbigeo(entity.getCodUbigeoProcedencia())
                    .build())
            .codEstadoCivil(
                    TablaMaestraResponse.builder()
                            .codigo(entity.getCodEstadoCivil())
                            .build()
            )
            .telefono(entity.getTelefono())
            .fotografia(entity.getFotografia())
            .urlFotografia(getUrlImage(entity.getFotografia(), entity.getUrlFotografia()))
            .estado(entity.getEstado())
            .build();

    public static final Function<DocenteEntity, DocenteResponse> toResponseDetail = entity -> DocenteResponse.builder()
            .docenteId(entity.getDocenteId())
            .codTipoDocumento(
                    TablaMaestraResponse.builder()
                            .codigo(entity.getCodTipoDocumento())
                            .build()
            )
            .numDocumento(entity.getNumDocumento())
            .nombre(entity.getNombre())
            .apellidoPaterno(entity.getApellidoPaterno())
            .apellidoMaterno(entity.getApellidoMaterno())
            .fechaNacimiento(DateConverterUtil.formatLocalDateToDDMMYYYY(entity.getFechaNacimiento()))
            .emailPersonal(entity.getEmailPersonal())
            .emailInstitucional(entity.getEmailInstitucional())
            .codGenero(
                    TablaMaestraResponse.builder()
                            .codigo(entity.getCodGenero())
                            .build()
            )
            .direccion(entity.getDireccion())
            .codUbigeoActual(UbigeoResponse.builder()
                    .codigoUbigeo(entity.getCodigoUbigeoActual())
                    .build())
            .codUbigeoProcedencia(UbigeoResponse.builder()
                    .codigoUbigeo(entity.getCodUbigeoProcedencia())
                    .build())
            .codEstadoCivil(
                    TablaMaestraResponse.builder()
                            .codigo(entity.getCodEstadoCivil())
                            .build()
            )
            .telefono(entity.getTelefono())
            .fotografia(entity.getFotografia())
            .urlFotografia(getUrlImage(entity.getFotografia(), entity.getUrlFotografia()))
            .estado(entity.getEstado())
            .infoAcademica(entity.getAcademicsInfo().stream().map(DocenteAcademicoResponse.toResponse).toList())
            .build();

    public static DocenteResponse toResponseWithMasterTable(
            DocenteEntity entity,
            TablaMaestraCache cache
    ) {
        String fotoUrl = null;

        if (entity.getFotografia() != null) {
            Path path = Paths.get(entity.getUrlFotografia());
            String nombreArchivo = path.getFileName().toString();

            fotoUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/docentes/imagenes/")
                    .path(nombreArchivo)
                    .toUriString();
        }

        return DocenteResponse.builder()
                .docenteId(entity.getDocenteId())
                .codTipoDocumento(TablaMaestraResponse.buildTablaMaestra(
                        cache.TIPO_DOCUMENTO_IDENTIDAD,
                        entity.getCodTipoDocumento(),
                        cache
                ))
                .codGenero(TablaMaestraResponse.buildTablaMaestra(
                        cache.GENERO,
                        entity.getCodGenero(),
                        cache
                ))
                .codEstadoCivil(TablaMaestraResponse.buildTablaMaestra(
                        cache.ESTADO_CIVIL,
                        entity.getCodEstadoCivil(),
                        cache
                ))
                .numDocumento(entity.getNumDocumento())
                .nombre(entity.getNombre())
                .apellidoPaterno(entity.getApellidoPaterno())
                .apellidoMaterno(entity.getApellidoMaterno())
                .fechaNacimiento(DateConverterUtil.formatLocalDateToDDMMYYYY(entity.getFechaNacimiento()))
                .emailPersonal(entity.getEmailPersonal())
                .emailInstitucional(entity.getEmailInstitucional())
                .direccion(entity.getDireccion())
                .codUbigeoActual(UbigeoResponse.builder()
                        .codigoUbigeo(entity.getCodigoUbigeoActual())
                        .build())
                .codUbigeoProcedencia(UbigeoResponse.builder()
                        .codigoUbigeo(entity.getCodUbigeoProcedencia())
                        .build())
                .telefono(entity.getTelefono())
                .fotografia(entity.getFotografia())
                .urlFotografia(fotoUrl)
                .estado(entity.getEstado())
                .infoAcademica(entity.getAcademicsInfo().stream()
                        .map(academicoEntity -> DocenteAcademicoResponse.toResponseWithMasterTableWithoutDocument(academicoEntity, cache))
                        .toList())
                .build();
    }

    public static String getUrlImage(String photo, String url) {
        String fotoUrl = null;

        if (photo != null) {
            Path path = Paths.get(url);
            String nombreArchivo = path.getFileName().toString();

            fotoUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/docentes/imagenes/")
                    .path(nombreArchivo)
                    .toUriString();
        }

        return fotoUrl;
    }


}
