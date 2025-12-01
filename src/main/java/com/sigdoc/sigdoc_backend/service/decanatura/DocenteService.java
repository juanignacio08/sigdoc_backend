package com.sigdoc.sigdoc_backend.service.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteEntity;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocenteRequest;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.DocenteResponse;
import org.springframework.data.domain.Page;

public interface DocenteService {
    DocenteResponse save(DocenteRequest request);
    Page<DocenteResponse> getAllPaginated(int page, int size);

    DocenteEntity getByDocumentNumber(String documentNumber);
}
