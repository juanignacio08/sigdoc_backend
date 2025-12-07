package com.sigdoc.sigdoc_backend.service.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteEntity;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocenteRequest;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.DocenteResponse;
import org.springframework.data.domain.Page;

public interface DocenteService {
    DocenteResponse save(DocenteRequest request);
    DocenteResponse update(DocenteRequest request);
    Page<DocenteResponse> getAllPaginated(int page, int size);
    Page<DocenteResponse> getByFilters(int page, int size, String typeDocument, String documentNumber, String name, String lastName1, String lastName2, String dateFirst, String dateEnd, String personalEmail, String institutionalEmail, String gender, String ubigeoActual, Integer nivelActual, String ubigeoProcedencia, Integer nivelProcedencia, String maritalStatus, Integer status, String dateCreationStart, String dateCreationEnd);
    DocenteResponse delete(String documentNumber);

    DocenteEntity getByDocumentNumber(String documentNumber);
    DocenteEntity getById(Long id);
}
