package com.sigdoc.sigdoc_backend.expose.response.maestro;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sigdoc.sigdoc_backend.entity.maestro.UbigeoEntity;
import com.sigdoc.sigdoc_backend.persistence.projections.UbigeoProjection;
import lombok.*;

import java.util.function.Function;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UbigeoResponse {

    private String regionId;
    private String region;

    private String provinciaId;
    private String provincia;

    private String ubigeo;
    private String distrito;

    public static Function<UbigeoProjection, UbigeoResponse> toResponseDepartaments = entity -> UbigeoResponse.builder()
            .regionId(entity.getRegionId())
            .region(entity.getRegion())
            .build();

    public static Function<UbigeoProjection, UbigeoResponse> toResponseProvinces = entity -> UbigeoResponse.builder()
            .regionId(entity.getRegionId())
            .region(entity.getRegion())
            .provinciaId(entity.getProvinciaId())
            .provincia(entity.getProvincia())
            .build();

    public static Function<UbigeoProjection, UbigeoResponse> toResponseDistricts = entity -> UbigeoResponse.builder()
            .regionId(entity.getRegionId())
            .region(entity.getRegion())
            .provinciaId(entity.getProvinciaId())
            .provincia(entity.getProvincia())
            .ubigeo(entity.getUbigeo())
            .distrito(entity.getDistrito())
            .build();

    public static Function<UbigeoEntity, UbigeoResponse> toResponse = entity -> UbigeoResponse.builder()
            .regionId(entity.getRegionId())
            .region(entity.getRegion())
            .provinciaId(entity.getProvinciaId())
            .provincia(entity.getProvincia())
            .ubigeo(entity.getUbigeo())
            .distrito(entity.getDistrito())
            .build();


}
