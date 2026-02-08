package com.sigdoc.sigdoc_backend.service.decanatura.impl;

import com.sigdoc.sigdoc_backend.entity.decanatura.*;
import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteLaboralEntity;
import com.sigdoc.sigdoc_backend.entity.maestro.DepartamentoAcademicoEntity;
import com.sigdoc.sigdoc_backend.exceptions.BadRequestException;
import com.sigdoc.sigdoc_backend.exceptions.BusinessRuleException;
import com.sigdoc.sigdoc_backend.exceptions.NotFoundException;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocenteLaboralRequest;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocumentoRequest;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.DocenteLaboralResponse;
import com.sigdoc.sigdoc_backend.expose.util.SecurityUtil;
import com.sigdoc.sigdoc_backend.persistence.repository.decanatura.DocenteLaboralRepository;
import com.sigdoc.sigdoc_backend.service.cache.TablaMaestraCache;
import com.sigdoc.sigdoc_backend.service.decanatura.DocenteLaboralService;
import com.sigdoc.sigdoc_backend.service.decanatura.DocenteService;
import com.sigdoc.sigdoc_backend.service.maestro.DepartamentoAcademicoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
@Service
public class DocenteLaboralServiceImpl implements DocenteLaboralService {

    private final DocenteLaboralRepository docenteLaboralRepository;

    private final DocenteService docenteService;
    private final DepartamentoAcademicoService departamentoAcademicoService;

    private final TablaMaestraCache tablaMaestraCache;

    @Override
    @Transactional
    public DocenteLaboralResponse save(DocenteLaboralRequest request) {
        String context = "saveWorkTeacher";
        log.info("Registrando datos laborales (institucionales) de un docente. [ CONTEXTO : {} ]", context);

        DocenteEntity docente = docenteService.getById(request.getDocenteId());

        DepartamentoAcademicoEntity departamentoAcademico = departamentoAcademicoService.getById(request.getDepartamentoId());

        if (docenteLaboralRepository.existsByDocenteId(request.getDocenteId())) throw new BusinessRuleException("El docente ya cuenta con una información laboral activa.\n" +
                "Para registrar una nueva, primero debe desactivar la información actual.");
        if (docenteLaboralRepository.existsByInstitutionalCode(request.getCodigoInstitucional())) throw new BusinessRuleException("El código institucional ya está registrado");
        if (docenteLaboralRepository.existsByInstitutionalEmail(request.getEmailInstitucional())) throw new BusinessRuleException("El email institucional ya está registrado");

        if (!tablaMaestraCache.existsItem(tablaMaestraCache.REGIMEN_LABORAL, request.getCodRegimenLaboral())) throw new BadRequestException("El regimen laboral ingresado no es válido. Por favor, verifique e inténtelo de nuevo.");
        if (request.getCodRegimenLaboral().equals(tablaMaestraCache.DOCENTE_NOMBRADO)) {
            if (request.getCodCategoriaDocenteNombrado() == null) throw new BusinessRuleException("La categoria del docente es requerida");
            if (!tablaMaestraCache.existsItem(tablaMaestraCache.CATEGORIA_DOCENTE, request.getCodCategoriaDocenteNombrado())) throw new BadRequestException("La categoria ingresada no es válido. Por favor, verifique e inténtelo de nuevo.");
            request.setCodNivelDocenteContratado(null);
        } else if (request.getCodRegimenLaboral().equals(tablaMaestraCache.DOCENTE_CONTRATADO)){
            if (request.getCodNivelDocenteContratado() == null) throw new BusinessRuleException("El nivel laboral del docente es requerida");
            if (!tablaMaestraCache.existsItem(tablaMaestraCache.NIVEL_DOCENTE, request.getCodNivelDocenteContratado())) throw new BadRequestException("El nivel ingresado no es válido. Por favor, verifique e inténtelo de nuevo.");
            request.setCodCategoriaDocenteNombrado(null);
        }
        if (request.getCodTiempoDedicacion() != null) if (!tablaMaestraCache.existsItem(tablaMaestraCache.TIEMPO_DEDICACION, request.getCodTiempoDedicacion())) throw new BadRequestException("El tiempo de dedicacion ingresado no es válido. Por favor, verifique e inténtelo de nuevo.");

        DocenteLaboralEntity entity = DocenteLaboralRequest.toEntity.apply(request);

        if (request.getDocumento() != null) {
            if (!tablaMaestraCache.existsItem(tablaMaestraCache.TIPO_DOCUMENTO, request.getDocumento().getCodTipoDocumento())) throw new BadRequestException("El tipo de documento seleccionado no es válido. Verifique la opción elegida y vuelva a intentarlo.");

            DocumentoEntity documento = DocumentoRequest.toEntity.apply(request.getDocumento());
            documento.setDocente(docente);
            entity.setDocumento(documento);
        }

        entity.setDocente(docente);
        entity.setDepartamentoAcademico(departamentoAcademico);

        DocenteLaboralEntity saved = docenteLaboralRepository.save(entity);

        return DocenteLaboralResponse.toResponse.apply(saved);

    }

    @Override
    @Transactional
    public DocenteLaboralResponse update(DocenteLaboralRequest request) {
        String context = "updateWorkTeacher";
        log.info("Actualizando los datos laborales de un docente. [ DOCENTE : {} | DOCENTE-LABORAL : {} | CONTEXTO : {} ]", request.getDocenteId(), request.getDocenteLaboralId(), context);

        DocenteLaboralEntity entity = getWithDocumentById(request.getDocenteLaboralId());

        if (docenteLaboralRepository.existsByDocenteId(request.getDocenteLaboralId(), request.getDocenteId())) throw new BusinessRuleException("El docente ya cuenta con una información laboral activa.\n" +
                "Para registrar una nueva, primero debe desactivar la información actual.");
        if (docenteLaboralRepository.existsByInstitutionalCode(request.getDocenteLaboralId(), request.getCodigoInstitucional())) throw new BusinessRuleException("El código institucional ya está registrado");
        if (docenteLaboralRepository.existsByInstitutionalEmail(request.getDocenteLaboralId(), request.getEmailInstitucional())) throw new BusinessRuleException("El email institucional ya está registrado");

        if (!tablaMaestraCache.existsItem(tablaMaestraCache.REGIMEN_LABORAL, request.getCodRegimenLaboral())) throw new BadRequestException("El regimen laboral ingresado no es válido. Por favor, verifique e inténtelo de nuevo.");
        if (request.getCodRegimenLaboral().equals(tablaMaestraCache.DOCENTE_NOMBRADO)) {
            if (request.getCodCategoriaDocenteNombrado() == null) throw new BusinessRuleException("La categoria del docente es requerida");
            if (!tablaMaestraCache.existsItem(tablaMaestraCache.CATEGORIA_DOCENTE, request.getCodCategoriaDocenteNombrado())) throw new BadRequestException("La categoria ingresada no es válido. Por favor, verifique e inténtelo de nuevo.");
            request.setCodNivelDocenteContratado(null);
        } else if (request.getCodRegimenLaboral().equals(tablaMaestraCache.DOCENTE_CONTRATADO)){
            if (request.getCodNivelDocenteContratado() == null) throw new BusinessRuleException("El nivel laboral del docente es requerida");
            if (!tablaMaestraCache.existsItem(tablaMaestraCache.NIVEL_DOCENTE, request.getCodNivelDocenteContratado())) throw new BadRequestException("El nivel ingresado no es válido. Por favor, verifique e inténtelo de nuevo.");
            request.setCodCategoriaDocenteNombrado(null);
        }
        if (request.getCodTiempoDedicacion() != null) if (!tablaMaestraCache.existsItem(tablaMaestraCache.TIEMPO_DEDICACION, request.getCodTiempoDedicacion())) throw new BadRequestException("El tiempo de dedicacion ingresado no es válido. Por favor, verifique e inténtelo de nuevo.");

        DocenteLaboralRequest.toUpdateEntity(request, entity);

        if (request.getDocumento() != null) {
            if (!tablaMaestraCache.existsItem(tablaMaestraCache.TIPO_DOCUMENTO, request.getDocumento().getCodTipoDocumento())) throw new BadRequestException("El tipo de documento seleccionado no es válido. Verifique la opción elegida y vuelva a intentarlo.");

            if (entity.getDocumento() != null) DocumentoRequest.toUpdateEntity(request.getDocumento(), entity.getDocumento());
            else {
                DocumentoEntity documento = DocumentoRequest.toEntity.apply(request.getDocumento());
                documento.setDocente(entity.getDocente());
                entity.setDocumento(documento);
            }
        }

        DocenteLaboralEntity updated = docenteLaboralRepository.save(entity);

        return DocenteLaboralResponse.toResponse.apply(updated);
    }

    @Override
    public Page<DocenteLaboralResponse> getPaginatedByDocente(int page, int size, String docenteNumberDocument) {
        String context = "getWorkTeacherByTeacher";
        log.info("Obteniendo informacion laboral de un docente. [ DOCENTE : {} | CONTEXTO : {} ]", docenteNumberDocument, context);

        DocenteEntity docente = docenteService.getByDocumentNumber(docenteNumberDocument);

        Pageable pageable = PageRequest.of(page, size);

        Page<DocenteLaboralEntity> entityPage = docenteLaboralRepository.getByDocentePaginated(docente.getDocenteId(), pageable);

        return entityPage.map(entity ->
            DocenteLaboralResponse.toResponseWithMasterTable(entity, tablaMaestraCache)
        );
    }

    @Override
    public DocenteLaboralResponse getWorkTeacherDetailById(Long id) {
        String context = "getWorkTeacherDetailById";
        log.info("Obteniendo informacion laborar a detalle. [ DOCENTE-LABORAL : {} | CONTEXTO : {} ]", id, context);

        DocenteLaboralEntity entity = getDetailById(id);

        return DocenteLaboralResponse.toResponseDetail.apply(entity);
    }

    @Override
    @Transactional
    public DocenteLaboralResponse delete(Long id) {
        String context = "deleteWorkTeacher";
        log.info("Eliminando informacion laboral de un docente. [ DOCENTE-LABORAL : {} | CONTEXTO : {} ]", id, context);

        DocenteLaboralEntity entity = getWithDocumentById(id);

        if (entity.getDocumento() != null) {
            entity.getDocumento().setEliminado(true);
            entity.getDocumento().setActualizadoPor(SecurityUtil.getCurrentUserId());
            entity.getDocumento().setFechaActualizacion(LocalDateTime.now());
        }

        entity.setEliminado(true);
        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());

        DocenteLaboralEntity deleted = docenteLaboralRepository.save(entity);

        return DocenteLaboralResponse.toResponse.apply(deleted);
    }

    @Override
    public DocenteLaboralResponse desactive(Long id) {
        String context = "desactiveWorkTeacher";
        log.info("Desactivando informacion laboral de un docente. [ DOCENTE-LABORAL : {} | CONTEXTO : {} ]", id, context);

        DocenteLaboralEntity entity = getWithDocumentById(id);

        entity.setEstado(0);
        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());

        DocenteLaboralEntity disabled = docenteLaboralRepository.save(entity);

        return DocenteLaboralResponse.toResponse.apply(disabled);
    }

    @Override
    public DocenteLaboralResponse active(Long id) {
        String context = "activeWorkTeacher";
        log.info("Habilitando informacion laboral de un docente. [ DOCENTE-LABORAL : {} | CONTEXTO : {} ]", id, context);

        DocenteLaboralEntity entity = getWithDocumentById(id);
        if (!canNewAdd(entity.getDocente().getDocenteId())) throw new BusinessRuleException("El docente ya cuenta con una información laboral activa.\n" +
                "Para dar de alta una información, primero debe desactivar todas las informaciónes.");

        entity.setEstado(1);
        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());

        DocenteLaboralEntity enabled = docenteLaboralRepository.save(entity);

        return DocenteLaboralResponse.toResponse.apply(enabled);
    }

    @Override
    public boolean canNewAdd(Long teacherId) {
        return !docenteLaboralRepository.existsByDocenteId(teacherId);
    }

    @Override
    public DocenteLaboralEntity getWithDocumentById(Long id) {
        if (id == null || id < 1) throw new BadRequestException("Id Incorrecto. [ DOCENTE-LABORAL ]");

        return docenteLaboralRepository.getWithDocumentByID(id)
                .orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ DOCENTE-LABORAL ]"));
    }

    @Override
    public DocenteLaboralEntity getDetailById(Long id) {
        if (id == null || id < 1) throw new BadRequestException("Id Incorrecto. [ DOCENTE-LABORAL ]");

        return docenteLaboralRepository.getDetailByID(id)
                .orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ DOCENTE-LABORAL ]"));
    }

}
