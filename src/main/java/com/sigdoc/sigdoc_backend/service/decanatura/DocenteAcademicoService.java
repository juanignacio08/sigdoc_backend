package com.sigdoc.sigdoc_backend.service.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteAcademicoEntity;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocenteAcademicoRequest;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.DocenteAcademicoResponse;
import org.springframework.data.domain.Page;

public interface DocenteAcademicoService {
    DocenteAcademicoResponse save(DocenteAcademicoRequest request);
    DocenteAcademicoResponse update(DocenteAcademicoRequest request);
    Page<DocenteAcademicoResponse> getPaginatedByDocente(int page, int size, String docenteNumberDocument);
    DocenteAcademicoResponse getAcademicTeacherDetailById(Long id);

    DocenteAcademicoEntity getWithDocumentById(Long id);
    DocenteAcademicoEntity getDetailById(Long id);
}
