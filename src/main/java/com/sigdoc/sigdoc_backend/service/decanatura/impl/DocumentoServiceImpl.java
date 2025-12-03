package com.sigdoc.sigdoc_backend.service.decanatura.impl;

import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocumentoRequest;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.DocumentoResponse;
import com.sigdoc.sigdoc_backend.persistence.repository.decanatura.DocumentoRepository;
import com.sigdoc.sigdoc_backend.service.decanatura.DocumentoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
@Service
public class DocumentoServiceImpl implements DocumentoService {

    private final DocumentoRepository documentoRepository;


    @Override
    @Transactional
    public DocumentoResponse save(DocumentoRequest request) {
        String context = "saveDocument";
        log.info("Registrando un documento de un docente. [ DOCENTE : {} | CONTEXTO : {} ]", request.getDocenteId(), context);

        

        return null;
    }
}
