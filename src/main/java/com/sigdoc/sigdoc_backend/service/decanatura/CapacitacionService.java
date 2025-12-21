package com.sigdoc.sigdoc_backend.service.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.CapacitacionEntity;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.CapacitacionRequest;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.CapacitacionResponse;
import org.springframework.data.domain.Page;

public interface CapacitacionService {
    CapacitacionResponse save(CapacitacionRequest request);
    CapacitacionResponse update(CapacitacionRequest request);
    Page<CapacitacionResponse> getPaginatedByDocente(int page, int size, String docenteNumberDocument);
    CapacitacionResponse getCapacitacionDetailById(Long id);
    CapacitacionResponse delete(Long id);

    CapacitacionEntity getWithDocumentById(Long id);
    CapacitacionEntity getDetailById(Long id);
}
