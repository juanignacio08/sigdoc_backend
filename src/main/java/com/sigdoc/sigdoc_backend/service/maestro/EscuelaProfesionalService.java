package com.sigdoc.sigdoc_backend.service.maestro;

import com.sigdoc.sigdoc_backend.entity.maestro.EscuelaProfesionalEntity;
import com.sigdoc.sigdoc_backend.expose.request.maestro.EscuelaProfesionalRequest;
import com.sigdoc.sigdoc_backend.expose.response.maestro.EscuelaProfesionalResponse;

import java.util.List;

public interface EscuelaProfesionalService {
    EscuelaProfesionalResponse save(EscuelaProfesionalRequest request);
    EscuelaProfesionalResponse update(EscuelaProfesionalRequest request);
    EscuelaProfesionalResponse getProfessionalSchoolById(Long id);
    List<EscuelaProfesionalResponse> getByAcademicApartment(Long academicApartment);
    EscuelaProfesionalResponse delete(Long id);

    EscuelaProfesionalEntity getById(Long id);
}
