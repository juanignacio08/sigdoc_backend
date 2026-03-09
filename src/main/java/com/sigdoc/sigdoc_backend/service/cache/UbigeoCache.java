package com.sigdoc.sigdoc_backend.service.cache;

import com.sigdoc.sigdoc_backend.entity.maestro.UbigeoEntity;
import com.sigdoc.sigdoc_backend.persistence.repository.maestro.UbigeoRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
public class UbigeoCache {

    private final UbigeoRepository ubigeoRepository;

    private Map<String, UbigeoEntity> cacheById;

    private Map<String, List<UbigeoEntity>> cacheProvinciasPorDepto;

    private Map<String, List<UbigeoEntity>> cacheDistritosPorProv;

    private List<UbigeoEntity> cacheDepartamentos;

    @PostConstruct
    public void loadCache() {
        log.info("================ Cargando cache de Ubigeo... ================");

        List<UbigeoEntity> all = ubigeoRepository.findAll();

        // Indexar por ID único para búsquedas específicas
        this.cacheById = all.stream()
                .collect(Collectors.toMap(UbigeoEntity::getUbigeo, u -> u));

        // Obtener Departamentos únicos (ordenados)
        this.cacheDepartamentos = all.stream()
                .filter(u -> u.getUbigeo().endsWith("0101")) // Truco: el primer distrito de cada depto
                .map(u -> {
                    UbigeoEntity depto = new UbigeoEntity();
                    depto.setRegionId(u.getRegionId());
                    depto.setRegion(u.getRegion());
                    return depto;
                })
                .distinct()
                .sorted(Comparator.comparing(UbigeoEntity::getRegion))
                .toList();

        // Agrupar Provincias por Departamento
        this.cacheProvinciasPorDepto = all.stream()
                .collect(Collectors.groupingBy(
                        UbigeoEntity::getRegionId,
                        Collectors.collectingAndThen(
                                Collectors.toMap(UbigeoEntity::getProvinciaId, u -> u, (u1, u2) -> u1),
                                m -> m.values().stream().sorted(Comparator.comparing(UbigeoEntity::getProvincia)).toList()
                        )
                ));

        // Agrupar Distritos por Provincia
        this.cacheDistritosPorProv = all.stream()
                .sorted(Comparator.comparing(UbigeoEntity::getDistrito))
                .collect(Collectors.groupingBy(UbigeoEntity::getProvinciaId));

        log.info("================ Cache de Ubigeo cargado correctamente. ================");
    }

    public boolean existsUbigeo(String ubigeoCode) {
        return cacheById.containsKey(ubigeoCode);
    }

    public void reload() {
        loadCache();
    }

    public List<UbigeoEntity> getDepartamentos() {
        return cacheDepartamentos;
    }

    // Para el segundo combo: Provincias de un Depto (ej. '05')
    public List<UbigeoEntity> getProvincias(String regionId) {
        return cacheProvinciasPorDepto.getOrDefault(regionId, Collections.emptyList());
    }

    // Para el tercer combo: Distritos de una Prov (ej. '0501')
    public List<UbigeoEntity> getDistritos(String provinciaId) {
        return cacheDistritosPorProv.getOrDefault(provinciaId, Collections.emptyList());
    }

    // Para obtener un distrito específico (para mostrar en un resumen)
    public UbigeoEntity getDistrito(String ubigeoCode) {
        return cacheById.get(ubigeoCode);
    }

}
