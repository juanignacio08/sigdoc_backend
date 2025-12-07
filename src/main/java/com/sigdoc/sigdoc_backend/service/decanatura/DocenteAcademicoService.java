package com.sigdoc.sigdoc_backend.service.decanatura;

import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocenteAcademicoRequest;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.DocenteAcademicoResponse;

public interface DocenteAcademicoService {
    DocenteAcademicoResponse save(DocenteAcademicoRequest request);

}
