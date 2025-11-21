package com.sigdoc.sigdoc_backend.service.maestro;

import com.sigdoc.sigdoc_backend.entity.maestro.TablaMaestraEntity;
import com.sigdoc.sigdoc_backend.expose.request.maestro.TablaMaestraRequest;
import com.sigdoc.sigdoc_backend.expose.response.maestro.TablaMaestraResponse;

import java.util.List;

public interface TablaMaestraService {
    TablaMaestraResponse save(TablaMaestraRequest request);
    TablaMaestraResponse update(TablaMaestraRequest request);
    List<TablaMaestraResponse> getAllTables();

    boolean existsItem(String code);
    TablaMaestraEntity getByCode(String code);
    List<TablaMaestraEntity> getTablesForMasterTable();
}
