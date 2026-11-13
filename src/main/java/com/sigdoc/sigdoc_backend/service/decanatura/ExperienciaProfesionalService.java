package com.sigdoc.sigdoc_backend.service.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.ExperienciaProfesionalEntity;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.ExperienciaProfesionalRequest;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.ExperienciaProfesionalResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface ExperienciaProfesionalService {
    ExperienciaProfesionalResponse save(ExperienciaProfesionalRequest request, MultipartFile file);
    ExperienciaProfesionalResponse update(ExperienciaProfesionalRequest request, MultipartFile file);
    Page<ExperienciaProfesionalResponse> getPaginatedByDocente(int page, int size, String docenteNumberDocument);
    ExperienciaProfesionalResponse getProfessionalExperienceDetailById(Long id);
    ExperienciaProfesionalResponse delete(Long id);
    Path getPdfPathById(Long id);

    ExperienciaProfesionalEntity getWithDocumentById(Long id);
    ExperienciaProfesionalEntity getDetailById(Long id);
}
