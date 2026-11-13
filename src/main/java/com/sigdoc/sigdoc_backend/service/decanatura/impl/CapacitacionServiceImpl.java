package com.sigdoc.sigdoc_backend.service.decanatura.impl;

import com.sigdoc.sigdoc_backend.entity.decanatura.*;
import com.sigdoc.sigdoc_backend.entity.decanatura.CapacitacionEntity;
import com.sigdoc.sigdoc_backend.exceptions.BadRequestException;
import com.sigdoc.sigdoc_backend.exceptions.NotFoundException;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocumentoRequest;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.CapacitacionRequest;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.CapacitacionResponse;
import com.sigdoc.sigdoc_backend.expose.util.SecurityUtil;
import com.sigdoc.sigdoc_backend.persistence.repository.decanatura.CapacitacionRepository;
import com.sigdoc.sigdoc_backend.service.cache.TablaMaestraCache;
import com.sigdoc.sigdoc_backend.service.decanatura.CapacitacionService;
import com.sigdoc.sigdoc_backend.service.decanatura.DocenteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
@Service
public class CapacitacionServiceImpl implements CapacitacionService {
    
    private final CapacitacionRepository capacitacionRepository;

    private final DocenteService docenteService;
    private final TablaMaestraCache tablaMaestraCache;
    
    @Override
    @Transactional
    public CapacitacionResponse save(CapacitacionRequest request) {
        String context = "saveCapacitacion";
        log.info("Registrando capacitacion. [ DOCENTE : {} | CONTEXTO : {} ]", request.getDocenteId(), context);

        DocenteEntity docente = docenteService.getById(request.getDocenteId());

        CapacitacionEntity entity = CapacitacionRequest.toEntity.apply(request);

        if (request.getDocumento() != null) {
            if (!tablaMaestraCache.existsItem(tablaMaestraCache.TIPO_DOCUMENTO, request.getDocumento().getCodTipoDocumento()))
                throw new BadRequestException("El tipo de documento seleccionado no es válido.");

            DocumentoEntity document = DocumentoRequest.toEntity.apply(request.getDocumento());
            document.setDocente(docente);
            entity.setDocumento(document);
        }

        entity.setDocente(docente);

        CapacitacionEntity saved = capacitacionRepository.save(entity);

        return CapacitacionResponse.toResponse.apply(saved);
    }

    @Override
    @Transactional
    public CapacitacionResponse update(CapacitacionRequest request) {
        String context = "updateCapacitacion";
        log.info("Actualizando capacitacion. [ DOCENTE : {} | CAPACITACION : {} | CONTEXTO : {} ]",
                request.getDocenteId(), request.getCapacitacionId(), context);

        CapacitacionEntity capacitacion = getWithDocumentById(request.getCapacitacionId());

        CapacitacionRequest.toUpdateEntity(request, capacitacion);

        if (request.getDocumento() != null) {
            if (!tablaMaestraCache.existsItem(tablaMaestraCache.TIPO_DOCUMENTO, request.getDocumento().getCodTipoDocumento()))
                throw new BadRequestException("El tipo de documento seleccionado no es válido.");

            if (capacitacion.getDocumento() != null) {
                DocumentoRequest.toUpdateEntity(request.getDocumento(), capacitacion.getDocumento());
            } else {
                DocumentoEntity documento = DocumentoRequest.toEntity.apply(request.getDocumento());
                documento.setDocente(capacitacion.getDocente());
                capacitacion.setDocumento(documento);
            }
        }

        CapacitacionEntity updated = capacitacionRepository.save(capacitacion);

        return CapacitacionResponse.toResponse.apply(updated);
    }

    @Override
    public Page<CapacitacionResponse> getPaginatedByDocente(int page, int size, String docenteNumberDocument) {
        String context = "getCapacitacionByDocente";
        log.info("Consultando Capacitacion. [ DOCENTE : {} | CONTEXTO : {} ]",
                docenteNumberDocument, context);

        DocenteEntity docente = docenteService.getByDocumentNumber(docenteNumberDocument);

        Pageable pageable = PageRequest.of(page, size);

        Page<CapacitacionEntity> entityPage =
                capacitacionRepository.getByDocentePaginated(docente.getDocenteId(), pageable);

        return entityPage.map(CapacitacionResponse.toResponse);
    }

    @Override
    public CapacitacionResponse getCapacitacionDetailById(Long id) {
        String context = "getCapacitacionDetail";
        log.info("Obteniendo detalle de Capacitacion. [ CAPACITACION : {} | CONTEXTO : {} ]", id, context);

        CapacitacionEntity capacitacion = getDetailById(id);

        return CapacitacionResponse.toResponseDetail.apply(capacitacion);
    }

    @Override
    @Transactional
    public CapacitacionResponse delete(Long id) {
        String context = "deleteCapacitacion";
        log.info("Eliminando Capacitacion. [ CAPACITACION : {} | CONTEXTO : {} ]", id, context);

        CapacitacionEntity capacitacion = getWithDocumentById(id);

        if (capacitacion.getDocumento() != null) {
            capacitacion.getDocumento().setEliminado(true);
            capacitacion.getDocumento().setActualizadoPor(SecurityUtil.getCurrentUserId());
            capacitacion.getDocumento().setFechaActualizacion(LocalDateTime.now());
        }

        capacitacion.setEliminado(true);
        capacitacion.setActualizadoPor(SecurityUtil.getCurrentUserId());
        capacitacion.setFechaActualizacion(LocalDateTime.now());

        CapacitacionEntity deleted = capacitacionRepository.save(capacitacion);

        return CapacitacionResponse.toResponse.apply(deleted);
    }

    @Override
    public CapacitacionEntity getWithDocumentById(Long id) {
        if (id == null || id < 1)
            throw new BadRequestException("Id incorrecto. [ CAPACITACION ]");

        return capacitacionRepository.getWithDocumentByID(id)
                .orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ CAPACITACION ]"));
    }

    @Override
    public CapacitacionEntity getDetailById(Long id) {
        if (id == null || id < 1)
            throw new BadRequestException("Id incorrecto. [ CAPACITACION ]");

        return capacitacionRepository.getDetailByID(id)
                .orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ CAPACITACION ]"));
    }
}
