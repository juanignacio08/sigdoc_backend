package com.sigdoc.sigdoc_backend.service.decanatura.impl;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteEntity;
import com.sigdoc.sigdoc_backend.exceptions.BadRequestException;
import com.sigdoc.sigdoc_backend.exceptions.DuplicateKeyErrorException;
import com.sigdoc.sigdoc_backend.exceptions.NotFoundException;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocenteFilter;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocenteRequest;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.DocenteResponse;
import com.sigdoc.sigdoc_backend.expose.util.DateConverterUtil;
import com.sigdoc.sigdoc_backend.expose.util.SecurityUtil;
import com.sigdoc.sigdoc_backend.persistence.repository.decanatura.DocenteRepository;
import com.sigdoc.sigdoc_backend.persistence.specification.DocenteSpecifications;
import com.sigdoc.sigdoc_backend.service.cache.TablaMaestraCache;
import com.sigdoc.sigdoc_backend.service.cache.UbigeoCache;
import com.sigdoc.sigdoc_backend.service.decanatura.DocenteService;
import com.sigdoc.sigdoc_backend.service.util.FileStorageService;
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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
@Service
public class DocenteServiceImpl implements DocenteService {

    private final DocenteRepository docenteRepository;

    private final FileStorageService fileStorageService;

    private final TablaMaestraCache tablaMaestraCache;
    private final UbigeoCache ubigeoCache;

    @Override
    @Transactional
    public DocenteResponse save(DocenteRequest request, MultipartFile file) {
        String context = "saveTeacher";
        log.info("Registrando a un nuevo profesor. [ DOCENTE : {} | CONTEXTO : {} ]", request.getNumDocumento(), context);

        DocenteEntity teacher;
        try {
            teacher = getByDocumentNumber(request.getNumDocumento());
        } catch (NotFoundException e) {
            teacher = null;
        }

        if (teacher != null) throw new DuplicateKeyErrorException("El docente ya esta registrado");

        if (!tablaMaestraCache.existsItem(tablaMaestraCache.TIPO_DOCUMENTO_IDENTIDAD, request.getCodTipoDocumento())) throw new BadRequestException("El tipo de documento es incorrecto.");

        if (StringUtils.hasText(request.getCodGenero()) && !tablaMaestraCache.existsItem(tablaMaestraCache.GENERO, request.getCodGenero())) throw new BadRequestException("El tipo de genero es incorrecto.");

        if (StringUtils.hasText(request.getCodEstadoCivil()) && !tablaMaestraCache.existsItem(tablaMaestraCache.ESTADO_CIVIL, request.getCodEstadoCivil())) throw new BadRequestException("El tipo de estado civil es incorrecto");

        if (StringUtils.hasText(request.getCodUbigeoActual()) && !ubigeoCache.existsUbigeo(request.getCodUbigeoActual())) throw new BadRequestException("El ubigeo de residencia actual es incorrecto.");

        if (StringUtils.hasText(request.getCodUbigeoProcedencia()) && !ubigeoCache.existsUbigeo(request.getCodUbigeoProcedencia())) throw new BadRequestException("El ubigeo de residencia de procedencia es incorrecto.");

        teacher = DocenteRequest.toEntity.apply(request);

        if (request.getFotografia() != null && file != null) {
            if (file.isEmpty()) {
                throw new BadRequestException("El archivo es obligatorio");
            }

            if (file.getSize() > 20 * 1024 * 1024) {
                throw new BadRequestException("El archivo no debe superar los 20 MB");
            }

            if (!file.getContentType().startsWith("image/")) {
                throw new BadRequestException("Solo se permiten archivos de imagen");
            }

            String path = fileStorageService.saveImage(file);
            teacher.setUrlFotografia(path);
        }

        DocenteEntity saved = docenteRepository.save(teacher);

        return DocenteResponse.toResponse.apply(saved);

    }

    @Override
    @Transactional
    public DocenteResponse update(DocenteRequest request, MultipartFile file) {
        String context = "updateTeacher";
        log.info("Actualizando los datos de un docente. [ DOCENTE : {} | CONTEXTO : {} ]", request.getNumDocumento(), context);

        DocenteEntity entity = getByDocumentNumber(request.getNumDocumento());

        if (!tablaMaestraCache.existsItem(tablaMaestraCache.TIPO_DOCUMENTO_IDENTIDAD, request.getCodTipoDocumento())) throw new BadRequestException("El tipo de documento es incorrecto.");

        if (StringUtils.hasText(request.getCodGenero()) && !tablaMaestraCache.existsItem(tablaMaestraCache.GENERO, request.getCodGenero())) throw new BadRequestException("El tipo de genero es incorrecto.");

        if (StringUtils.hasText(request.getCodEstadoCivil()) && !tablaMaestraCache.existsItem(tablaMaestraCache.ESTADO_CIVIL, request.getCodEstadoCivil())) throw new BadRequestException("El tipo de estado civil es incorrecto");

        if (StringUtils.hasText(request.getCodUbigeoActual()) && !ubigeoCache.existsUbigeo(request.getCodUbigeoActual())) throw new BadRequestException("El ubigeo de residencia actual es incorrecto.");

        if (StringUtils.hasText(request.getCodUbigeoProcedencia()) && !ubigeoCache.existsUbigeo(request.getCodUbigeoProcedencia())) throw new BadRequestException("El ubigeo de residencia de procedencia es incorrecto.");

        DocenteRequest.toUpdateEntity(request, entity);

        if (request.getFotografia() != null && file != null) {
            if (file.isEmpty()) {
                throw new BadRequestException("El archivo es obligatorio");
            }

            if (file.getSize() > 20 * 1024 * 1024) {
                throw new BadRequestException("El archivo no debe superar los 20 MB");
            }

            if (!file.getContentType().startsWith("image/")) {
                throw new BadRequestException("Solo se permiten archivos de imagen");
            }

            if (entity.getFotografia() != null) {
                String rutaAnterior = entity.getUrlFotografia();

                String path = fileStorageService.saveImage(file);
                entity.setUrlFotografia(path);

                if (rutaAnterior != null) {
                    try {
                        Path pathDelete = Paths.get(rutaAnterior);
                        Files.deleteIfExists(pathDelete);
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
            } else {
                String path = fileStorageService.saveImage(file);
                entity.setUrlFotografia(path);
            }
        }

        DocenteEntity updated = docenteRepository.save(entity);

        return DocenteResponse.toResponse.apply(updated);
    }

    @Override
    public Page<DocenteResponse> getAllPaginated(int page, int size) {
        String context = "getPaginated";
        log.info("Obteniendo docentes. [ CONTEXTO : {} ]", context);

        Pageable pageable = PageRequest.of(page, size);

        Page<DocenteEntity> entityPage = docenteRepository.getAllPaginated(pageable);

        return entityPage.map(
                entity ->
                        DocenteResponse.toResponseWithMasterTable(entity, tablaMaestraCache)
        );
    }

    @Override
    public Page<DocenteResponse> getByFilters(int page, int size, DocenteFilter filter) {
        String context = "getByFilters";
        log.info("Realizando busquedas mediante filtros. [ CONTEXTO : {} ]", context);

        LocalDate startDateBirthdate = filter.birthdateStart() == null ? null : DateConverterUtil.parseFechaDDMMYYYY(filter.birthdateStart());
        LocalDate endDateBirthdate = filter.birthdateEnd() == null ? null : DateConverterUtil.parseFechaDDMMYYYY(filter.birthdateEnd());

        LocalDate startDateCreation = filter.dateCreationStart() == null ? null : DateConverterUtil.parseFechaDDMMYYYY(filter.dateCreationStart());
        LocalDate endDateCreation = filter.dateCreationEnd() == null ? null : DateConverterUtil.parseFechaDDMMYYYY(filter.dateCreationEnd());

        LocalDate startDateInstitutional = filter.startDate() == null ? null : DateConverterUtil.parseFechaDDMMYYYY(filter.startDate());
        LocalDate endDateInstitutional = filter.endDate() == null ? null : DateConverterUtil.parseFechaDDMMYYYY(filter.endDate());

        Specification<DocenteEntity> specification = Specification.allOf(
                DocenteSpecifications.notRemoved(),
                DocenteSpecifications.byDocumentType(filter.typeDocument()),
                DocenteSpecifications.byDocumentNumber(filter.documentNumber()),
                DocenteSpecifications.byName(filter.name()),
                DocenteSpecifications.byLastName1(filter.lastName1()),
                DocenteSpecifications.byLastName2(filter.lastName2()),
                DocenteSpecifications.byBirthdateBetween(startDateBirthdate, endDateBirthdate),
                DocenteSpecifications.byPersonalEmail(filter.personalEmail()),
                DocenteSpecifications.byInstitutionalEmail(filter.institutionalEmail()),
                DocenteSpecifications.byGender(filter.gender()),
                DocenteSpecifications.byUbigeoActual(filter.ubigeoActual(), filter.nivelActual()),
                DocenteSpecifications.byUbigeoProcedencia(filter.ubigeoProcedencia(), filter.nivelProcedencia()),
                DocenteSpecifications.byMaritalStatus(filter.maritalStatus()),
                DocenteSpecifications.status(filter.status()),
                DocenteSpecifications.byCreationDateBetween(startDateCreation, endDateCreation),
                DocenteSpecifications.byInstitutionalFilters(filter.departmentAcademicId(), filter.institutionalCode(), filter.institutionalEmail(), filter.workRegimeCode(), filter.categoryCode(), filter.levelCode(), filter.dedicationTime(), startDateInstitutional, endDateInstitutional),
                DocenteSpecifications.byAcademicFilters(filter.academicGrade(), filter.professionalTitle(), filter.academicInstitute(), filter.academicSpecialty()),
                DocenteSpecifications.byLanguageFilters(filter.language(), filter.languageLevel(), filter.languageInstitute()),
                DocenteSpecifications.byExperienceFilters(filter.company(), filter.cargo())
        );

        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaActualizacion").descending());

        Page<DocenteEntity> entityPage = docenteRepository.findAll(specification, pageable);

        return entityPage.map(entity ->
                DocenteResponse.toResponseWithMasterTable(entity, tablaMaestraCache)
        );
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
    public DocenteResponse getDetail(String documentNumber) {
        DocenteEntity docente = docenteRepository.getDetailByID(documentNumber).orElseThrow(() -> new NotFoundException("No existe el docente"));

        return DocenteResponse.toResponseDetail.apply(docente);
    }

    @Override
    public Path getImagePathById(Long id) {
        DocenteEntity entity = getById(id);

        if (entity.getUrlFotografia() == null) return null;

        return Paths.get(entity.getUrlFotografia());
    }

    @Override
    public DocenteEntity getByDocumentNumber(String documentNumber) {
        return docenteRepository.getByDocumentNumber(documentNumber)
                .orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ DOCENTE ]"));
    }

    @Override
    public DocenteEntity getById(Long id) {
        if (id == null || id < 1) throw new BadRequestException("Id Incorrecto. [ DOCENTE ]");

        return docenteRepository.getByID(id)
                .orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ DOCENTE ]"));
    }



}
