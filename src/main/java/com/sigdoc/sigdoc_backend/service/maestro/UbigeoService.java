package com.sigdoc.sigdoc_backend.service.maestro;

import com.sigdoc.sigdoc_backend.expose.response.maestro.UbigeoResponse;

import java.util.List;

public interface UbigeoService {
    List<UbigeoResponse> getDepartamentos();
    List<UbigeoResponse> getProvincias(String codeDepartamento);
    List<UbigeoResponse> getDistritos(String codeProvincia);
    UbigeoResponse getUbigeo(String code);
}
