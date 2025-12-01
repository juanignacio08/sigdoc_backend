package com.sigdoc.sigdoc_backend.service.cache;

import com.sigdoc.sigdoc_backend.entity.maestro.UbigeoEntity;
import com.sigdoc.sigdoc_backend.persistence.repository.maestro.UbigeoRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
public class UbigeoCache {

    private final UbigeoRepository ubigeoRepository;

    private Map<String, String> cacheUbigeo;

    @PostConstruct
    public void loadCache() {
        log.info("================ Cargando cache de Ubigeo... ================");

        cacheUbigeo = new HashMap<>();

        List<UbigeoEntity> registros = ubigeoRepository.getAll();

        for (UbigeoEntity u : registros) {
            cacheUbigeo.computeIfAbsent(u.getCodigoUbigeo(), k -> u.getDenominacion());
        }

        log.info("================ Cache de Ubigeo cargado correctamente. ================");
    }

    public boolean existsUbigeo(String ubigeoCode) {
        return cacheUbigeo.containsKey(ubigeoCode);
    }

    public void reload() {
        loadCache();
    }

}
