package com.sigdoc.sigdoc_backend.service.maestro.impl;

import com.sigdoc.sigdoc_backend.entity.maestro.UbigeoEntity;
import com.sigdoc.sigdoc_backend.exceptions.NotFoundException;
import com.sigdoc.sigdoc_backend.expose.response.maestro.UbigeoResponse;
import com.sigdoc.sigdoc_backend.persistence.repository.maestro.UbigeoRepository;
import com.sigdoc.sigdoc_backend.service.cache.UbigeoCache;
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

    private final UbigeoCache ubigeoCache;

    @Override
    public List<UbigeoResponse> getDepartamentos() {
        String context = "getDepartaments";
        log.info("Obteniendo departamentos. [ CONTEXTO : {} ]", context);

        // List<UbigeoProjection> list = ubigeoRepository.getDepartamentos();
        List<UbigeoEntity> list = ubigeoCache.getDepartamentos();

        return list.stream()
                .map(UbigeoResponse.toResponse)
                .toList();
    }

    @Override
    public List<UbigeoResponse> getProvincias(String codeDepartamento) {
        String context = "getProvinces";
        log.info("Obteniendo Provincias de un departamento. [ DEPARTAMENTO : {} | CONTEXTO : {} ]", codeDepartamento, context);

        //List<UbigeoProjection> list = ubigeoRepository.getProvincias(codeDepartamento);
        List<UbigeoEntity> list = ubigeoCache.getProvincias(codeDepartamento);

        return list.stream().map(UbigeoResponse.toResponse).toList();
    }

    @Override
    public List<UbigeoResponse> getDistritos(String codeProvincia) {
        String context = "getProvinces";
        log.info("Obteniendo Distritos de una provincia. [ PROVINCIA : {} | CONTEXTO : {} ]", codeProvincia, context);

        //List<UbigeoProjection> list = ubigeoRepository.getDistritos(codeProvincia);
        List<UbigeoEntity> list = ubigeoCache.getDistritos(codeProvincia);

        return list.stream().map(UbigeoResponse.toResponse).toList();
    }

    @Override
    public UbigeoResponse getUbigeo(String code) {
        String context = "getUbigeo";
        log.info("Obteniendo la informacion a detalle de un ubigeo. [ UBIGEO : {} | CONTEXT : {} ]", code, context);

        UbigeoEntity entity = ubigeoCache.getDistrito(code);

        if (entity == null) throw new NotFoundException("Registro no encontrado. [ UBIGEO ]");

        //UbigeoEntity entity = ubigeoRepository.findByUbigeo(code)
        //        .orElseThrow(() -> new NotFoundException("Registro no encontrado. [ UBIGEO ]"));

        return UbigeoResponse.toResponse.apply(entity);
    }

}
