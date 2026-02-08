package com.sigdoc.sigdoc_backend.service.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteAcademicoEntity;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocenteAcademicoRequest;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.DocenteAcademicoResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface DocenteAcademicoService {
    DocenteAcademicoResponse save(DocenteAcademicoRequest request, MultipartFile file);
    DocenteAcademicoResponse update(DocenteAcademicoRequest request, MultipartFile file);
    Page<DocenteAcademicoResponse> getPaginatedByDocente(int page, int size, String docenteNumberDocument);
    DocenteAcademicoResponse getAcademicTeacherDetailById(Long id);
    DocenteAcademicoResponse delete(Long id);
    Path getPdfPathById(Long id);

    DocenteAcademicoEntity getWithDocumentById(Long id);
    DocenteAcademicoEntity getDetailById(Long id);
}
