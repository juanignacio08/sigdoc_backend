package com.sigdoc.sigdoc_backend.expose.response.maestro;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sigdoc.sigdoc_backend.entity.maestro.UbigeoEntity;
import lombok.*;

import java.util.function.Function;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UbigeoResponse {

    private String codigoUbigeo;

    private String codigoRegion;
    private String denominacionRegion;

    private String codigoProvincia;
    private String denominacionProvincia;

    private String codigoDistrito;
    private String denominacionDistrito;

    private Integer estado;

    public static Function<UbigeoEntity, UbigeoResponse> toResponseDepartaments = entity -> UbigeoResponse.builder()
            .codigoUbigeo(entity.getCodigoUbigeo())
            .codigoRegion(entity.getCodigoRegion())
            .denominacionRegion(entity.getDenominacion())
            .estado(entity.getEstado())
            .build();

    public static Function<UbigeoEntity, UbigeoResponse> toResponseProvinces = entity -> UbigeoResponse.builder()
            .codigoUbigeo(entity.getCodigoUbigeo())
            .codigoRegion(entity.getCodigoRegion())
            .codigoProvincia(entity.getCodigoProvincia())
            .denominacionProvincia(entity.getDenominacion())
            .estado(entity.getEstado())
            .build();

    public static Function<UbigeoEntity, UbigeoResponse> toResponseDistricts = entity -> UbigeoResponse.builder()
            .codigoUbigeo(entity.getCodigoUbigeo())
            .codigoRegion(entity.getCodigoRegion())
            .codigoProvincia(entity.getCodigoProvincia())
            .codigoDistrito(entity.getCodigoDistrito())
            .denominacionDistrito(entity.getDenominacion())
            .estado(entity.getEstado())
            .build();


}
