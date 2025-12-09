package com.sigdoc.sigdoc_backend.service.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteIdiomaEntity;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocenteIdiomaRequest;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.DocenteIdiomaResponse;
import org.springframework.data.domain.Page;

public interface DocenteIdiomaService {

    DocenteIdiomaResponse save(DocenteIdiomaRequest request);
    DocenteIdiomaResponse update(DocenteIdiomaRequest request);
    Page<DocenteIdiomaResponse> getPaginatedByDocente(int page, int size, String docenteNumberDocument);
    DocenteIdiomaResponse getLanguageTeacherDetailById(Long id);
    DocenteIdiomaResponse delete(Long id);

    DocenteIdiomaEntity getWithDocumentById(Long id);
    DocenteIdiomaEntity getDetailById(Long id);

}
