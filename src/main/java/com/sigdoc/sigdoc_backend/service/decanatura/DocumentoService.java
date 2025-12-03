package com.sigdoc.sigdoc_backend.service.decanatura;

import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocumentoRequest;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.DocumentoResponse;

public interface DocumentoService {
    DocumentoResponse save(DocumentoRequest request);
}
