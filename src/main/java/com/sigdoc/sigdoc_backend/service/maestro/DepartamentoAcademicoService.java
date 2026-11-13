package com.sigdoc.sigdoc_backend.service.maestro;

import com.sigdoc.sigdoc_backend.entity.maestro.DepartamentoAcademicoEntity;
import com.sigdoc.sigdoc_backend.expose.request.maestro.DepartamentoAcademicoRequest;
import com.sigdoc.sigdoc_backend.expose.response.maestro.DepartamentoAcademicoResponse;

import java.util.List;

public interface DepartamentoAcademicoService {
    DepartamentoAcademicoResponse save(DepartamentoAcademicoRequest request);
    DepartamentoAcademicoResponse update(DepartamentoAcademicoRequest request);
    DepartamentoAcademicoResponse getAcademicDepartamentById(Long id);
    List<DepartamentoAcademicoResponse> getByFaculty(Long faculty);
    DepartamentoAcademicoResponse delete(Long id);

    DepartamentoAcademicoEntity getById(Long id);
}
