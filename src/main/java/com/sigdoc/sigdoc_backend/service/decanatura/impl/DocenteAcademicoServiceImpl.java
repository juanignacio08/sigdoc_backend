package com.sigdoc.sigdoc_backend.service.decanatura.impl;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteAcademicoEntity;
import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteEntity;
import com.sigdoc.sigdoc_backend.entity.decanatura.DocumentoEntity;
import com.sigdoc.sigdoc_backend.exceptions.BadRequestException;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocenteAcademicoRequest;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocumentoRequest;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.DocenteAcademicoResponse;
import com.sigdoc.sigdoc_backend.persistence.repository.decanatura.DocenteAcademicoRepository;
import com.sigdoc.sigdoc_backend.service.cache.TablaMaestraCache;
import com.sigdoc.sigdoc_backend.service.decanatura.DocenteAcademicoService;
import com.sigdoc.sigdoc_backend.service.decanatura.DocenteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
@Service
public class DocenteAcademicoServiceImpl implements DocenteAcademicoService {

    private final DocenteAcademicoRepository docenteAcademicoRepository;

    private final DocenteService docenteService;

    private final TablaMaestraCache tablaMaestraCache;

    @Override
    @Transactional
    public DocenteAcademicoResponse save(DocenteAcademicoRequest request) {
        String context = "saveDocenteAcademico";
        log.info("Registrando datos academicos de un docente. [ DOCENTE : {} | CONTEXTO : {} ]", request.getDocenteId(), context);

        DocenteEntity docente = docenteService.getById(request.getDocenteId());

        if (!tablaMaestraCache.existsItem(tablaMaestraCache.GRADO_ACADEMICO, request.getCodGradoAcademico())) throw new BadRequestException("El grado academico es incorrecto.");

        if (!tablaMaestraCache.existsItem(tablaMaestraCache.TIPO_DOCUMENTO, request.getDocumento().getCodTipoDocumento())) throw new BadRequestException("El tipo de documento es incorrecto.");

        DocenteAcademicoEntity entity = DocenteAcademicoRequest.toEntity.apply(request);

        DocumentoEntity documento = DocumentoRequest.toEntity.apply(request.getDocumento());
        
        entity.setDocente(docente);
        entity.setDocumento(documento);

        DocenteAcademicoEntity saved = docenteAcademicoRepository.save(entity);

        return DocenteAcademicoResponse.toResponse.apply(saved);

    }
}
