package com.sigdoc.sigdoc_backend.service.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteEntity;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocenteRequest;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.DocenteResponse;

public interface DocenteService {
    DocenteResponse save(DocenteRequest request);

    DocenteEntity getByDocumentNumber(String documentNumber);
}
