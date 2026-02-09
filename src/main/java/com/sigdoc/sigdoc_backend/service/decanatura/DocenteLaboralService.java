package com.sigdoc.sigdoc_backend.service.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteLaboralEntity;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocenteLaboralRequest;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.DocenteLaboralResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface DocenteLaboralService {
    DocenteLaboralResponse save(DocenteLaboralRequest request, MultipartFile file);
    DocenteLaboralResponse update(DocenteLaboralRequest request);
    Page<DocenteLaboralResponse> getPaginatedByDocente(int page, int size, String docenteNumberDocument);
    DocenteLaboralResponse getWorkTeacherDetailById(Long id);
    DocenteLaboralResponse delete(Long id);
    DocenteLaboralResponse desactive(Long id);
    DocenteLaboralResponse active(Long id);
    boolean canNewAdd(Long teacherId);
    Path getPdfPathById(Long id);

    DocenteLaboralEntity getWithDocumentById(Long id);
    DocenteLaboralEntity getDetailById(Long id);
}
