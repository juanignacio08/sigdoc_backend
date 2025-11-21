package com.sigdoc.sigdoc_backend.service.maestro.impl;

import com.sigdoc.sigdoc_backend.entity.maestro.TablaMaestraEntity;
import com.sigdoc.sigdoc_backend.exceptions.BadRequestException;
import com.sigdoc.sigdoc_backend.exceptions.DuplicateKeyErrorException;
import com.sigdoc.sigdoc_backend.exceptions.NotFoundException;
import com.sigdoc.sigdoc_backend.expose.request.maestro.TablaMaestraRequest;
import com.sigdoc.sigdoc_backend.expose.response.maestro.TablaMaestraResponse;
import com.sigdoc.sigdoc_backend.persistence.repository.maestro.TablaMaestraRepository;
import com.sigdoc.sigdoc_backend.service.maestro.TablaMaestraService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class TablaMaestraServiceImpl implements TablaMaestraService {

    private final TablaMaestraRepository tablaMaestraRepository;

    @Override
    @Transactional
    public TablaMaestraResponse save(TablaMaestraRequest request) {
        String context = "saveMasterTable";
        log.info("Registrando un registro de tabla maestra. [ CONTEXTO : {} ]", context);

        TablaMaestraEntity entity = TablaMaestraRequest.toEntity.apply(request);

        if (existsItem(entity.getCodigo())) throw new DuplicateKeyErrorException("Ya existe un registro con ese codigo: " + entity.getCodigo());

        TablaMaestraEntity saved = tablaMaestraRepository.save(entity);

        return TablaMaestraResponse.toResponse.apply(saved);
    }

    @Override
    @Transactional
    public TablaMaestraResponse update(TablaMaestraRequest request) {
        String context = "updateMasterTable";
        log.info("Actualizando un registro de tabla maestra. [ TABLA_MAESTRA : {} | CONTEXTO : {} ]", request.getCodigo(), context);

        TablaMaestraEntity entity = getByCode(request.getCodigo());

        TablaMaestraRequest.toUpdateEntity(request, entity);

        TablaMaestraEntity saved = tablaMaestraRepository.save(entity);

        return TablaMaestraResponse.toResponse.apply(saved);
    }

    @Override
    public List<TablaMaestraResponse> getAllTables() {
        String context = "getAllTables";
        log.info("Obteniendo todas las tablas registradas en tabla maestra. [ CONTEXTO : {} ]", context);

        List<TablaMaestraEntity> list = getTablesForMasterTable();

        return list.stream()
                .map(TablaMaestraResponse.toResponse)
                .toList();
    }

    @Override
    public List<TablaMaestraResponse> getAllTablesByIsSystem(boolean isSystem) {
        String context = "getAllTablesByIsSystem";
        log.info("Obteniendo todas las tablas registradas en tabla maestra por el filtro de si es sistema o no. [ CONTEXTO : {} ]", context);

        List<TablaMaestraEntity> list = getTablesByIsSystem(isSystem);

        return list.stream()
                .map(TablaMaestraResponse.toResponse)
                .toList();
    }

    @Override
    public List<TablaMaestraResponse> getAllItemsFromTable(String tableCode) {
        String context = "getAllItemsFromTable";
        log.info("Obteniendo los items de una tabla. [ TABLA : {} | CONTEXTO : {} ]", tableCode, context);

        List<TablaMaestraEntity> list = getItemsFromTable(tableCode);

        return list.stream()
                .map(TablaMaestraResponse.toResponse)
                .toList();
    }

    @Override
    public List<TablaMaestraResponse> getAllActivesItemsFromTable(String tableCode) {
        String context = "getAllActivesItemsFromTable";
        log.info("Obteniendo los items activos de una tabla. [ TABLA : {} | CONTEXTO : {} ]", tableCode, context);

        List<TablaMaestraEntity> list = getActivesItemsFromTable(tableCode);

        return list.stream()
                .map(TablaMaestraResponse.toResponse)
                .toList();
    }

    @Override
    public boolean existsItem(String code) {
        return tablaMaestraRepository.existsMasterTable(code);
    }

    @Override
    public TablaMaestraEntity getByCode(String code) {
        if (code == null || code.length() < 6) throw new BadRequestException("Codigo Incorrecto. [ TABLA_MAESTRA ]");

        return tablaMaestraRepository.getByCode(code)
                .orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ TABLA_MAESTRA ]"));
    }

    @Override
    public List<TablaMaestraEntity> getTablesForMasterTable() {
        return tablaMaestraRepository.getTables();
    }

    @Override
    public List<TablaMaestraEntity> getTablesByIsSystem(boolean isSystem) {
        return tablaMaestraRepository.getTablesByIsSystem(isSystem);
    }

    @Override
    public List<TablaMaestraEntity> getItemsFromTable(String tableCode) {
        if (tableCode == null || tableCode.length() != 3) throw new BadRequestException("Codigo de Tabla incorrecto. [ TABLA_MAESTRA ]");

        return tablaMaestraRepository.getAllItemsByTable(tableCode);
    }

    @Override
    public List<TablaMaestraEntity> getActivesItemsFromTable(String tableCode) {
        if (tableCode == null || tableCode.length() != 3) throw new BadRequestException("Codigo de Tabla incorrecto. [ TABLA_MAESTRA ]");

        return tablaMaestraRepository.getAllActivesItemsByTable(tableCode);
    }
}
