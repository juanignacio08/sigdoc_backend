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

import java.util.ArrayList;
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
    public List<UbigeoResponse> getDistritos(String codeProvincia) {
        return List.of();
    }

    @Override
    public UbigeoResponse getUbigeo(String code) {
        return null;
    }

}
