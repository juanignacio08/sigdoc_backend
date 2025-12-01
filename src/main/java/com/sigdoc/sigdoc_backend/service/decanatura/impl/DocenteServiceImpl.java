package com.sigdoc.sigdoc_backend.service.decanatura.impl;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteEntity;
import com.sigdoc.sigdoc_backend.exceptions.NotFoundException;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocenteRequest;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.DocenteResponse;
import com.sigdoc.sigdoc_backend.persistence.repository.decanatura.DocenteRepository;
import com.sigdoc.sigdoc_backend.service.decanatura.DocenteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
@Service
public class DocenteServiceImpl implements DocenteService {

    private final DocenteRepository docenteRepository;

    @Override
    @Transactional
    public DocenteResponse save(DocenteRequest request) {
        String context = "saveTeacher";
        log.info("Registrando a un nuevo profesor. [ DOCENTE : {} | CONTEXTO : {} ]", request.getNumDocumento(), context);

        DocenteEntity teacher;
        try {
            teacher = getByDocumentNumber(request.getNumDocumento());
        } catch (NotFoundException e) {
            teacher = null;
        }

        if (teacher != null) return DocenteResponse.toResponse.apply(teacher);

        teacher = DocenteRequest.toEntity.apply(request);

        DocenteEntity saved = docenteRepository.save(teacher);

        return DocenteResponse.toResponse.apply(saved);

    }

    @Override
    public DocenteEntity getByDocumentNumber(String documentNumber) {
        return docenteRepository.getByDocumentNumber(documentNumber)
                .orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ DOCENTE ]"));
    }
}
