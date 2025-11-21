package com.sigdoc.sigdoc_backend.service.maestro;

import com.sigdoc.sigdoc_backend.entity.maestro.TablaMaestraEntity;
import com.sigdoc.sigdoc_backend.expose.request.maestro.TablaMaestraRequest;
import com.sigdoc.sigdoc_backend.expose.response.maestro.TablaMaestraResponse;

public interface TablaMaestraService {
    TablaMaestraResponse save(TablaMaestraRequest request);
    TablaMaestraResponse update(TablaMaestraRequest request);

    boolean existsItem(String code);
    TablaMaestraEntity getByCode(String code);
}
