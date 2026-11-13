package com.sigdoc.sigdoc_backend.service.cache;

import com.sigdoc.sigdoc_backend.entity.maestro.TablaMaestraEntity;
import com.sigdoc.sigdoc_backend.persistence.repository.maestro.TablaMaestraRepository;
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
public class TablaMaestraCache {

    private final TablaMaestraRepository tablaMaestraRepository;

    private Map<String, List<TablaMaestraEntity>> cacheByTable;
    private Map<String, TablaMaestraEntity> cacheByCode;

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
        log.info("Cargando doble caché de Tabla Maestra...");
        List<TablaMaestraEntity> allItems = tablaMaestraRepository.getAll();

        this.cacheByCode = allItems.stream()
                .collect(Collectors.toMap(
                        TablaMaestraEntity::getCodigo,
                        item -> item
                ));

        this.cacheByTable = allItems.stream()
                .filter(item -> !item.getCodigoItem().equals("000"))
                .sorted(Comparator.comparing(TablaMaestraEntity::getOrden))
                .collect(Collectors.groupingBy(TablaMaestraEntity::getCodigoTabla));

        log.info("Caché optimizado correctamente.");
    }

    public boolean existsItem(String tableCode, String code) {
        List<TablaMaestraEntity> tableItems = cacheByTable.get(tableCode);

        if (tableItems == null) {
            return false;
        }

        return tableItems.stream()
                .anyMatch(item -> item.getCodigo().equals(code));
    }

    public TablaMaestraEntity getItem(String code) {
        return cacheByCode.get(code);
    }

    public List<TablaMaestraEntity> getItemsByTableCode(String tableCode) {
        return cacheByTable.getOrDefault(tableCode, Collections.emptyList());
    }

    public void reload() {
        loadCache();
    }
}
