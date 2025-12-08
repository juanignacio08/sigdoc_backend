package com.sigdoc.sigdoc_backend.service.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteAcademicoEntity;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocenteAcademicoRequest;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.DocenteAcademicoResponse;

public interface DocenteAcademicoService {
    DocenteAcademicoResponse save(DocenteAcademicoRequest request);
    DocenteAcademicoResponse update(DocenteAcademicoRequest request);

    DocenteAcademicoEntity getWithDocumentById(Long id);
}
