package com.sigdoc.sigdoc_backend.service.cache;

import com.sigdoc.sigdoc_backend.entity.maestro.TablaMaestraEntity;
import com.sigdoc.sigdoc_backend.persistence.repository.maestro.TablaMaestraRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
public class TablaMaestraCache {

    private final TablaMaestraRepository tablaMaestraRepository;

    private Map<String, Map<String, String>> cacheMasterTable;

    public final String TIPO_DOCUMENTO_IDENTIDAD = "001";
    public final String GENERO = "002";
    public final String ESTADO_CIVIL = "003";
    public final String GRADO_ACADEMICO = "004";
    public final String TIPO_DOCUMENTO = "005";
    public final String IDIOMA = "006";
    public final String NIVEL_IDIOMA = "007";
    public final String REGIMEN_LABORAL = "008";
    public final String CATEGORIA_DOCENTE = "009";
    public final String NIVEL_DOCENTE = "010";
    public final String TIEMPO_DEDICACION = "011";

    public final String DOCENTE_NOMBRADO = "008001";
    public final String DOCENTE_CONTRATADO = "008002";

    @PostConstruct
    public void loadCache() {
        log.info("================ Cargando cache de Tabla Maestra... ================");

        cacheMasterTable = new HashMap<>();

        List<TablaMaestraEntity> registros = tablaMaestraRepository.getAll();

        for (TablaMaestraEntity t : registros) {
            cacheMasterTable.computeIfAbsent(t.getCodigoTabla(), k -> new HashMap<>())
                    .put(t.getCodigo(), t.getDenominacion());
        }

        log.info("================ Cache de Tabla Maestra cargado correctamente. ================");
    }

    public boolean existsItem(String tableCode, String itemCode) {
        return cacheMasterTable.containsKey(tableCode)
                && cacheMasterTable.get(tableCode).containsKey(itemCode);
    }

    public String getDenominacion(String tableCode, String itemCode) {
        return Optional.ofNullable(cacheMasterTable.get(tableCode))
                .map(m -> m.get(itemCode))
                .orElse(null);
    }

    public void reload() {
        loadCache();
    }
}
