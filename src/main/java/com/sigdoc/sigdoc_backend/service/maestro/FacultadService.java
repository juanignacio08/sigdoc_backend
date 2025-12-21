package com.sigdoc.sigdoc_backend.service.maestro;

import com.sigdoc.sigdoc_backend.entity.maestro.FacultadEntity;
import com.sigdoc.sigdoc_backend.expose.request.maestro.FacultadRequest;
import com.sigdoc.sigdoc_backend.expose.response.maestro.FacultadResponse;

import java.util.List;

public interface FacultadService {
    FacultadResponse save(FacultadRequest request);
    FacultadResponse update(FacultadRequest request);
    List<FacultadResponse> getList();
    FacultadResponse getFacultadById(Long id);
    FacultadResponse delete(Long id);

    FacultadEntity getById(Long id);
}
