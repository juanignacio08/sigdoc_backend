package com.sigdoc.sigdoc_backend.service.decanatura.impl;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteEntity;
import com.sigdoc.sigdoc_backend.exceptions.BadRequestException;
import com.sigdoc.sigdoc_backend.exceptions.NotFoundException;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocenteRequest;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.DocenteResponse;
import com.sigdoc.sigdoc_backend.expose.util.DateConverterUtil;
import com.sigdoc.sigdoc_backend.expose.util.SecurityUtil;
import com.sigdoc.sigdoc_backend.persistence.repository.decanatura.DocenteRepository;
import com.sigdoc.sigdoc_backend.persistence.specification.DocenteSpecifications;
import com.sigdoc.sigdoc_backend.service.cache.TablaMaestraCache;
import com.sigdoc.sigdoc_backend.service.cache.UbigeoCache;
import com.sigdoc.sigdoc_backend.service.decanatura.DocenteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    @Transactional
    public DocenteResponse update(DocenteRequest request) {
        String context = "updateTeacher";
        log.info("Actualizando los datos de un docente. [ DOCENTE : {} | CONTEXTO : {} ]", request.getNumDocumento(), context);

        DocenteEntity entity = getByDocumentNumber(request.getNumDocumento());

        if (!tablaMaestraCache.existsItem(tablaMaestraCache.TIPO_DOCUMENTO, request.getCodTipoDocumento())) throw new BadRequestException("El tipo de documento es incorrecto.");

        if (!tablaMaestraCache.existsItem(tablaMaestraCache.GENERO, request.getCodGenero())) throw new BadRequestException("El tipo de genero es incorrecto.");

        if (!tablaMaestraCache.existsItem(tablaMaestraCache.ESTADO_CIVIL, request.getCodEstadoCivil())) throw new BadRequestException("El tipo de estado civil es incorrecto");

        if (!ubigeoCache.existsUbigeo(request.getCodUbigeoActual())) throw new BadRequestException("El ubigeo de residencia actual es incorrecto.");

        if (!ubigeoCache.existsUbigeo(request.getCodUbigeoProcedencia())) throw new BadRequestException("El ubigeo de residencia de procedencia es incorrecto.");

        DocenteRequest.toUpdateEntity(request, entity);

        DocenteEntity updated = docenteRepository.save(entity);

        return DocenteResponse.toResponse.apply(updated);
    }

    @Override
    public Page<DocenteResponse> getAllPaginated(int page, int size) {
        String context = "getPaginated";
        log.info("Obteniendo docentes. [ CONTEXTO : {} ]", context);

        Pageable pageable = PageRequest.of(page, size);

        Page<DocenteEntity> entityPage = docenteRepository.getAllPaginated(pageable);

        return entityPage.map(DocenteResponse.toResponse);
    }

    @Override
    public Page<DocenteResponse> getByFilters(int page, int size, String typeDocument, String documentNumber, String name, String lastName1, String lastName2, String dateFirst, String dateEnd, String personalEmail, String institutionalEmail, String gender, String ubigeoActual, Integer nivelActual, String ubigeoProcedencia, Integer nivelProcedencia, String maritalStatus, Integer status, String dateCreationStart, String dateCreationEnd) {
        String context = "getByFilters";
        log.info("Realizando busquedas mediante filtros. [ CONTEXTO : {} ]", context);

        LocalDate startDateBirthdate = dateFirst == null ? null : DateConverterUtil.parseFechaDDMMYYYY(dateFirst);
        LocalDate endDateBirthdate = dateEnd == null ? null : DateConverterUtil.parseFechaDDMMYYYY(dateEnd);

        LocalDate startDateCreation = dateCreationStart == null ? null : DateConverterUtil.parseFechaDDMMYYYY(dateCreationStart);
        LocalDate endDateCreation = dateCreationEnd == null ? null : DateConverterUtil.parseFechaDDMMYYYY(dateCreationEnd);

        Specification<DocenteEntity> specification = Specification.allOf(
                DocenteSpecifications.notRemoved(),
                DocenteSpecifications.byDocumentType(typeDocument),
                DocenteSpecifications.byDocumentNumber(documentNumber),
                DocenteSpecifications.byName(name),
                DocenteSpecifications.byLastName1(lastName1),
                DocenteSpecifications.byLastName2(lastName2),
                DocenteSpecifications.byBirthdateBetween(startDateBirthdate, endDateBirthdate),
                DocenteSpecifications.byPersonalEmail(personalEmail),
                DocenteSpecifications.byInstitutionalEmail(institutionalEmail),
                DocenteSpecifications.byGender(gender),
                DocenteSpecifications.byUbigeoActual(ubigeoActual, nivelActual),
                DocenteSpecifications.byUbigeoProcedencia(ubigeoProcedencia, nivelProcedencia),
                DocenteSpecifications.byMaritalStatus(maritalStatus),
                DocenteSpecifications.status(status),
                DocenteSpecifications.byCreationDateBetween(startDateCreation, endDateCreation)
        );

        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaActualizacion").descending());

        Page<DocenteEntity> entityPage = docenteRepository.findAll(specification, pageable);

        return entityPage.map(DocenteResponse.toResponse);
    }

    @Override
    @Transactional
    public DocenteResponse delete(String documentNumber) {
        String context = "deleteTeacher";
        log.info("Eliminando a un docente. [ NUMERO_DOCUMENTO : {} | CONTEXTO : {} ]", documentNumber, context);

        DocenteEntity entity = getByDocumentNumber(documentNumber);

        entity.setEliminado(true);
        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());

        DocenteEntity deleted = docenteRepository.save(entity);

        return DocenteResponse.toResponse.apply(deleted);
    }

    @Override
    public DocenteEntity getByDocumentNumber(String documentNumber) {
        return docenteRepository.getByDocumentNumber(documentNumber)
                .orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ DOCENTE ]"));
    }
}
