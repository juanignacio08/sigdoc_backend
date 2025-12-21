package com.sigdoc.sigdoc_backend.service.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.ExperienciaProfesionalEntity;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.ExperienciaProfesionalRequest;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.ExperienciaProfesionalResponse;
import org.springframework.data.domain.Page;

public interface ExperienciaProfesionalService {
    ExperienciaProfesionalResponse save(ExperienciaProfesionalRequest request);
    ExperienciaProfesionalResponse update(ExperienciaProfesionalRequest request);
    Page<ExperienciaProfesionalResponse> getPaginatedByDocente(int page, int size, String docenteNumberDocument);
    ExperienciaProfesionalResponse getProfessionalExperienceDetailById(Long id);
    ExperienciaProfesionalResponse delete(Long id);

    ExperienciaProfesionalEntity getWithDocumentById(Long id);
    ExperienciaProfesionalEntity getDetailById(Long id);
}
