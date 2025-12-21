package com.sigdoc.sigdoc_backend.service.decanatura.impl;

import com.sigdoc.sigdoc_backend.entity.decanatura.ExperienciaProfesionalEntity;
import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteEntity;
import com.sigdoc.sigdoc_backend.entity.decanatura.DocumentoEntity;
import com.sigdoc.sigdoc_backend.exceptions.BadRequestException;
import com.sigdoc.sigdoc_backend.exceptions.NotFoundException;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.ExperienciaProfesionalRequest;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocumentoRequest;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.ExperienciaProfesionalResponse;
import com.sigdoc.sigdoc_backend.expose.util.SecurityUtil;
import com.sigdoc.sigdoc_backend.persistence.repository.decanatura.ExperienciaProfesionalRepository;
import com.sigdoc.sigdoc_backend.service.cache.TablaMaestraCache;
import com.sigdoc.sigdoc_backend.service.decanatura.ExperienciaProfesionalService;
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
public class ExperienciaProfesionalServiceImpl implements ExperienciaProfesionalService {

    private final ExperienciaProfesionalRepository experienciaProfesionalRepository;

    private final DocenteService docenteService;
    private final TablaMaestraCache tablaMaestraCache;

    @Override
    @Transactional
    public ExperienciaProfesionalResponse save(ExperienciaProfesionalRequest request) {
        String context = "saveExperienciaProfesional";
        log.info("Registrando experiencia profesional. [ DOCENTE : {} | CONTEXTO : {} ]", request.getDocenteId(), context);

        DocenteEntity docente = docenteService.getById(request.getDocenteId());

        ExperienciaProfesionalEntity entity = ExperienciaProfesionalRequest.toEntity.apply(request);

        if (request.getDocumento() != null) {
            if (!tablaMaestraCache.existsItem(tablaMaestraCache.TIPO_DOCUMENTO, request.getDocumento().getCodTipoDocumento()))
                throw new BadRequestException("El tipo de documento seleccionado no es válido.");

            DocumentoEntity document = DocumentoRequest.toEntity.apply(request.getDocumento());
            document.setDocente(docente);
            entity.setDocumento(document);
        }

        entity.setDocente(docente);

        ExperienciaProfesionalEntity saved = experienciaProfesionalRepository.save(entity);

        return ExperienciaProfesionalResponse.toResponse.apply(saved);
    }

    @Override
    @Transactional
    public ExperienciaProfesionalResponse update(ExperienciaProfesionalRequest request) {
        String context = "updateExperienciaProfesional";
        log.info("Actualizando experiencia profesional. [ DOCENTE : {} | EXPERIENCIA : {} | CONTEXTO : {} ]",
                request.getDocenteId(), request.getExperienciaProfesionalId(), context);

        ExperienciaProfesionalEntity experience = getWithDocumentById(request.getExperienciaProfesionalId());

        ExperienciaProfesionalRequest.toUpdateEntity(request, experience);

        if (request.getDocumento() != null) {
            if (!tablaMaestraCache.existsItem(tablaMaestraCache.TIPO_DOCUMENTO, request.getDocumento().getCodTipoDocumento()))
                throw new BadRequestException("El tipo de documento seleccionado no es válido.");

            if (experience.getDocumento() != null) {
                DocumentoRequest.toUpdateEntity(request.getDocumento(), experience.getDocumento());
            } else {
                DocumentoEntity documento = DocumentoRequest.toEntity.apply(request.getDocumento());
                documento.setDocente(experience.getDocente());
                experience.setDocumento(documento);
            }
        }

        ExperienciaProfesionalEntity updated = experienciaProfesionalRepository.save(experience);

        return ExperienciaProfesionalResponse.toResponse.apply(updated);
    }

    @Override
    public Page<ExperienciaProfesionalResponse> getPaginatedByDocente(int page, int size, String numeroDocumentoDocente) {
        String context = "getExperienciaByDocente";
        log.info("Consultando experiencia profesional. [ DOCENTE : {} | CONTEXTO : {} ]",
                numeroDocumentoDocente, context);

        DocenteEntity docente = docenteService.getByDocumentNumber(numeroDocumentoDocente);

        Pageable pageable = PageRequest.of(page, size);

        Page<ExperienciaProfesionalEntity> entityPage =
                experienciaProfesionalRepository.getByDocentePaginated(docente.getDocenteId(), pageable);

        return entityPage.map(ExperienciaProfesionalResponse.toResponse);
    }

    @Override
    public ExperienciaProfesionalResponse getProfessionalExperienceDetailById(Long id) {
        String context = "getExperienciaDetail";
        log.info("Obteniendo detalle de experiencia profesional. [ EXPERIENCIA : {} | CONTEXTO : {} ]", id, context);

        ExperienciaProfesionalEntity experiencia = getDetailById(id);

        return ExperienciaProfesionalResponse.toResponseDetail.apply(experiencia);
    }

    @Override
    @Transactional
    public ExperienciaProfesionalResponse delete(Long id) {
        String context = "deleteExperienciaProfesional";
        log.info("Eliminando experiencia profesional. [ EXPERIENCIA : {} | CONTEXTO : {} ]", id, context);

        ExperienciaProfesionalEntity experiencia = getWithDocumentById(id);

        if (experiencia.getDocumento() != null) {
            experiencia.getDocumento().setEliminado(true);
            experiencia.getDocumento().setActualizadoPor(SecurityUtil.getCurrentUserId());
            experiencia.getDocumento().setFechaActualizacion(LocalDateTime.now());
        }

        experiencia.setEliminado(true);
        experiencia.setActualizadoPor(SecurityUtil.getCurrentUserId());
        experiencia.setFechaActualizacion(LocalDateTime.now());

        ExperienciaProfesionalEntity deleted = experienciaProfesionalRepository.save(experiencia);

        return ExperienciaProfesionalResponse.toResponse.apply(deleted);
    }

    @Override
    public ExperienciaProfesionalEntity getWithDocumentById(Long id) {
        if (id == null || id < 1)
            throw new BadRequestException("Id incorrecto. [ EXPERIENCIA-PROFESIONAL ]");

        return experienciaProfesionalRepository.getWithDocumentByID(id)
                .orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ EXPERIENCIA-PROFESIONAL ]"));
    }

    @Override
    public ExperienciaProfesionalEntity getDetailById(Long id) {
        if (id == null || id < 1)
            throw new BadRequestException("Id incorrecto. [ EXPERIENCIA-PROFESIONAL ]");

        return experienciaProfesionalRepository.getDetailByID(id)
                .orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ EXPERIENCIA-PROFESIONAL ]"));
    }

}
