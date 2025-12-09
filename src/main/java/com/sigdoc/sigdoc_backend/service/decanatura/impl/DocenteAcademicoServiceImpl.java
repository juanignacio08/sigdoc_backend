package com.sigdoc.sigdoc_backend.service.decanatura.impl;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteAcademicoEntity;
import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteEntity;
import com.sigdoc.sigdoc_backend.entity.decanatura.DocumentoEntity;
import com.sigdoc.sigdoc_backend.exceptions.BadRequestException;
import com.sigdoc.sigdoc_backend.exceptions.NotFoundException;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocenteAcademicoRequest;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocumentoRequest;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.DocenteAcademicoResponse;
import com.sigdoc.sigdoc_backend.expose.util.SecurityUtil;
import com.sigdoc.sigdoc_backend.persistence.repository.decanatura.DocenteAcademicoRepository;
import com.sigdoc.sigdoc_backend.service.cache.TablaMaestraCache;
import com.sigdoc.sigdoc_backend.service.decanatura.DocenteAcademicoService;
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

        if (!tablaMaestraCache.existsItem(tablaMaestraCache.GRADO_ACADEMICO, request.getCodGradoAcademico())) throw new BadRequestException("El grado académico ingresado no es válido. Por favor, verifique e inténtelo de nuevo.");

        DocenteAcademicoEntity entity = DocenteAcademicoRequest.toEntity.apply(request);

        if (request.getDocumento() != null) {
            if (!tablaMaestraCache.existsItem(tablaMaestraCache.TIPO_DOCUMENTO, request.getDocumento().getCodTipoDocumento())) throw new BadRequestException("El tipo de documento seleccionado no es válido. Verifique la opción elegida y vuelva a intentarlo.");

            DocumentoEntity documento = DocumentoRequest.toEntity.apply(request.getDocumento());
            documento.setDocente(docente);
            entity.setDocumento(documento);
        }

        entity.setDocente(docente);

        DocenteAcademicoEntity saved = docenteAcademicoRepository.save(entity);

        return DocenteAcademicoResponse.toResponse.apply(saved);

    }

    @Override
    @Transactional
    public DocenteAcademicoResponse update(DocenteAcademicoRequest request) {
        String context = "updateTeacherAcademic";
        log.info("Actualizando los datos academicos de un docente. [ DOCENTE : {} | DOCENTE-ACADEMICO : {} | CONTEXTO : {} ]", request.getDocenteId(), request.getDocenteAcademicoId(), context);

        DocenteAcademicoEntity docenteAcademico = getWithDocumentById(request.getDocenteAcademicoId());

        if (!tablaMaestraCache.existsItem(tablaMaestraCache.GRADO_ACADEMICO, request.getCodGradoAcademico())) throw new BadRequestException("El grado académico ingresado no es válido. Por favor, verifique e inténtelo de nuevo.");

        DocenteAcademicoRequest.toUpdateEntity(request, docenteAcademico);

        if (request.getDocumento() != null) {
            if (!tablaMaestraCache.existsItem(tablaMaestraCache.TIPO_DOCUMENTO, request.getDocumento().getCodTipoDocumento())) throw new BadRequestException("El tipo de documento seleccionado no es válido. Verifique la opción elegida y vuelva a intentarlo.");

            if (docenteAcademico.getDocumento() != null) DocumentoRequest.toUpdateEntity(request.getDocumento(), docenteAcademico.getDocumento());
            else {
                DocumentoEntity documento = DocumentoRequest.toEntity.apply(request.getDocumento());
                documento.setDocente(docenteAcademico.getDocente());
                docenteAcademico.setDocumento(documento);
            }
        }

        DocenteAcademicoEntity updated = docenteAcademicoRepository.save(docenteAcademico);

        return DocenteAcademicoResponse.toResponse.apply(updated);
    }

    @Override
    public Page<DocenteAcademicoResponse> getPaginatedByDocente(int page, int size, String docenteNumberDocument) {
        String context = "getTeacherAcademicByTeacher";
        log.info("Obteniendo informacion academica de un docente. [ DOCENTE : {} | CONTEXTO : {} ]", docenteNumberDocument, context);

        DocenteEntity docente = docenteService.getByDocumentNumber(docenteNumberDocument);

        Pageable pageable = PageRequest.of(page, size);

        Page<DocenteAcademicoEntity> entityPage = docenteAcademicoRepository.getByDocentePaginated(docente.getDocenteId(), pageable);

        return entityPage.map(DocenteAcademicoResponse.toResponse);
    }

    @Override
    public DocenteAcademicoResponse getAcademicTeacherDetailById(Long id) {
        String context = "getAcademicTeacherDetailById";
        log.info("Obteniendo a detalle un grado academico. [ DOCENTE-ACADEMIC : {} | CONTEXTO : {} ]", id, context);

        DocenteAcademicoEntity docenteAcademico = getDetailById(id);

        return DocenteAcademicoResponse.toResponseDetail.apply(docenteAcademico);
    }

    @Override
    @Transactional
    public DocenteAcademicoResponse delete(Long id) {
        String context = "deleteAcademicTeacher";
        log.info("Eliminando informacion academica de un docente. [ DOCENTE-ACADEMICO : {} | CONTEXTO : {} ]", id, context);

        DocenteAcademicoEntity docenteAcademico = getWithDocumentById(id);

        if (docenteAcademico.getDocumento() != null) {
            docenteAcademico.getDocumento().setEliminado(true);
            docenteAcademico.getDocumento().setActualizadoPor(SecurityUtil.getCurrentUserId());
            docenteAcademico.getDocumento().setFechaActualizacion(LocalDateTime.now());
        }

        docenteAcademico.setEliminado(true);
        docenteAcademico.setActualizadoPor(SecurityUtil.getCurrentUserId());
        docenteAcademico.setFechaActualizacion(LocalDateTime.now());

        DocenteAcademicoEntity deleted = docenteAcademicoRepository.save(docenteAcademico);

        return DocenteAcademicoResponse.toResponse.apply(deleted);
    }

    @Override
    public DocenteAcademicoEntity getWithDocumentById(Long id) {
        if (id == null || id < 1) throw new BadRequestException("Id Incorrecto. [ DOCENTE-ACADEMICO ]");

        return docenteAcademicoRepository.getWithDocumentByID(id).orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ DOCENTE-ACADEMICO ]"));
    }

    @Override
    public DocenteAcademicoEntity getDetailById(Long id) {
        if (id == null || id < 1) throw new BadRequestException("Id Incorrecto. [ DOCENTE-ACADEMICO ]");

        return docenteAcademicoRepository.getDetailByID(id).orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ DOCENTE-ACADEMICO ]"));
    }
}
