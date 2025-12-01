package com.sigdoc.sigdoc_backend.service.decanatura.impl;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteEntity;
import com.sigdoc.sigdoc_backend.exceptions.BadRequestException;
import com.sigdoc.sigdoc_backend.exceptions.NotFoundException;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocenteRequest;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.DocenteResponse;
import com.sigdoc.sigdoc_backend.persistence.repository.decanatura.DocenteRepository;
import com.sigdoc.sigdoc_backend.service.cache.TablaMaestraCache;
import com.sigdoc.sigdoc_backend.service.cache.UbigeoCache;
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

    private final TablaMaestraCache tablaMaestraCache;
    private final UbigeoCache ubigeoCache;

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

        if (!tablaMaestraCache.existsItem(tablaMaestraCache.TIPO_DOCUMENTO, request.getCodTipoDocumento())) throw new BadRequestException("El tipo de documento es incorrecto.");

        if (!tablaMaestraCache.existsItem(tablaMaestraCache.GENERO, request.getCodGenero())) throw new BadRequestException("El tipo de genero es incorrecto.");

        if (!tablaMaestraCache.existsItem(tablaMaestraCache.ESTADO_CIVIL, request.getCodEstadoCivil())) throw new BadRequestException("El tipo de estado civil es incorrecto");

        if (!ubigeoCache.existsUbigeo(request.getCodUbigeoActual())) throw new BadRequestException("El ubigeo de residencia actual es incorrecto.");

        if (!ubigeoCache.existsUbigeo(request.getCodUbigeoProcedencia())) throw new BadRequestException("El ubigeo de residencia de procedencia es incorrecto.");

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
