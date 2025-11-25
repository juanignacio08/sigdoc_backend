package com.sigdoc.sigdoc_backend.service.maestro.impl;

import com.sigdoc.sigdoc_backend.entity.maestro.UbigeoEntity;
import com.sigdoc.sigdoc_backend.exceptions.NotFoundException;
import com.sigdoc.sigdoc_backend.expose.response.maestro.UbigeoResponse;
import com.sigdoc.sigdoc_backend.persistence.repository.maestro.UbigeoRepository;
import com.sigdoc.sigdoc_backend.service.maestro.UbigeoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
@Service
public class UbigeoServiceImpl implements UbigeoService {

    private final UbigeoRepository ubigeoRepository;

    @Override
    public List<UbigeoResponse> getDepartamentos() {
        String context = "getDepartaments";
        log.info("Obteniendo departamentos. [ CONTEXTO : {} ]", context);

        List<UbigeoEntity> list = ubigeoRepository.getDepartamentos();

        return list.stream()
                .map(UbigeoResponse.toResponseDepartaments)
                .toList();
    }

    @Override
    public List<UbigeoResponse> getProvincias(String codeDepartamento) {
        String context = "getProvinces";
        log.info("Obteniendo Provincias de un departamento. [ DEPARTAMENTO : {} | CONTEXTO : {} ]", codeDepartamento, context);

        if (!ubigeoRepository.existsDepartamentByCode(codeDepartamento)) throw new NotFoundException("Recurso no encontrado. [ DEPARTAMENTO ]");

        List<UbigeoEntity> list = ubigeoRepository.getProvinces(codeDepartamento);

        return list.stream().map(UbigeoResponse.toResponseProvinces).toList();
    }

    @Override
    public List<UbigeoResponse> getDistritos(String codeDepartamento, String codeProvincia) {
        String context = "getProvinces";
        log.info("Obteniendo Provincias de un departamento. [ DEPARTAMENTO : {} | PROVINCIA : {} | CONTEXTO : {} ]", codeDepartamento, codeProvincia, context);

        if (!ubigeoRepository.existsProvinceByCode(codeDepartamento, codeProvincia)) throw new NotFoundException("Recurso no encontrado. [ PROVINCIA ]");

        List<UbigeoEntity> list = ubigeoRepository.getDistricts(codeDepartamento, codeProvincia);

        return list.stream().map(UbigeoResponse.toResponseDistricts).toList();
    }

    @Override
    public UbigeoResponse getUbigeo(String code) {
        String context = "getUbigeo";
        log.info("Obteniendo la informacion a detalle de un ubigeo. [ UBIGEO : {} | CONTEXT : {} ]", code, context);

        String departamentCode = code.substring(0, 2);
        String provinceCode = code.substring(2, 4);

        UbigeoEntity entity = ubigeoRepository.getByCode(code)
                .orElseThrow(() -> new NotFoundException("Registro no encontrado. [ UBIGEO ]"));

        UbigeoEntity departament = ubigeoRepository.getDepartamentByCode(departamentCode)
                .orElseThrow(() -> new NotFoundException("Registro no encontrado. [ DEPARTAMENTO ]"));

        UbigeoEntity province = ubigeoRepository.getProvinceByCode(departamentCode, provinceCode)
                .orElseThrow(() -> new NotFoundException("Registro no encontrado. [ PROVINCIA ]"));

        return UbigeoResponse.builder()
                .codigoUbigeo(entity.getCodigoUbigeo())
                .codigoRegion(departament.getCodigoRegion())
                .denominacionRegion(departament.getDenominacion())
                .codigoProvincia(province.getCodigoProvincia())
                .denominacionProvincia(province.getDenominacion())
                .codigoDistrito(entity.getCodigoDistrito())
                .denominacionDistrito(entity.getDenominacion())
                .estado(entity.getEstado())
                .build();
    }

}
