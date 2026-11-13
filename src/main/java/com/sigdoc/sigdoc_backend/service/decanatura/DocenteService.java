package com.sigdoc.sigdoc_backend.service.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteEntity;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocenteFilter;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocenteRequest;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.DocenteResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface DocenteService {
    DocenteResponse save(DocenteRequest request, MultipartFile file);
    DocenteResponse update(DocenteRequest request, MultipartFile file);
    Page<DocenteResponse> getAllPaginated(int page, int size);
    Page<DocenteResponse> getByFilters(int page, int size, DocenteFilter filter);
    DocenteResponse delete(String documentNumber);
    DocenteResponse getDetail(String documentNumber);
    Path getImagePathById(Long id);

    DocenteEntity getByDocumentNumber(String documentNumber);
    DocenteEntity getById(Long id);
}
