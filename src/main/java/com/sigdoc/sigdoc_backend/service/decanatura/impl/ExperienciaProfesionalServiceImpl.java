package com.sigdoc.sigdoc_backend.service.decanatura.impl;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteAcademicoEntity;
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
import com.sigdoc.sigdoc_backend.service.util.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
@Service
public class ExperienciaProfesionalServiceImpl implements ExperienciaProfesionalService {

    private final ExperienciaProfesionalRepository experienciaProfesionalRepository;

    private final DocenteService docenteService;
    private final FileStorageService fileStorageService;

    private final TablaMaestraCache tablaMaestraCache;

    @Override
    @Transactional
    public ExperienciaProfesionalResponse save(ExperienciaProfesionalRequest request, MultipartFile file) {
        String context = "saveExperienciaProfesional";
        log.info("Registrando experiencia profesional. [ DOCENTE : {} | CONTEXTO : {} ]", request.getDocenteId(), context);

        DocenteEntity docente = docenteService.getById(request.getDocenteId());

        ExperienciaProfesionalEntity entity = ExperienciaProfesionalRequest.toEntity.apply(request);
        entity.setDocente(docente);

        if (request.getDocumento() != null && file != null) {
            if (!tablaMaestraCache.existsItem(tablaMaestraCache.TIPO_DOCUMENTO, request.getDocumento().getCodTipoDocumento()))
                throw new BadRequestException("El tipo de documento seleccionado no es válido.");

            if (file.getSize() > 20 * 1024 * 1024) {
                throw new BadRequestException("El archivo no debe superar los 20 MB");
            }

            if (!"application/pdf".equals(file.getContentType())) {
                throw new BadRequestException("Solo se permiten archivos PDF");
            }

            String path = fileStorageService.savePdf(file, request.getDocenteId());

            DocumentoEntity document = DocumentoRequest.toEntity.apply(request.getDocumento());
            document.setUrl(path);
            document.setDocente(docente);

            entity.setDocumento(document);
        }

        ExperienciaProfesionalEntity saved = experienciaProfesionalRepository.save(entity);

        return ExperienciaProfesionalResponse.toResponse.apply(saved);
    }

    @Override
    @Transactional
    public ExperienciaProfesionalResponse update(ExperienciaProfesionalRequest request, MultipartFile file) {
        String context = "updateExperienciaProfesional";
        log.info("Actualizando experiencia profesional. [ DOCENTE : {} | EXPERIENCIA : {} | CONTEXTO : {} ]",
                request.getDocenteId(), request.getExperienciaProfesionalId(), context);

        ExperienciaProfesionalEntity experience = getWithDocumentById(request.getExperienciaProfesionalId());

        ExperienciaProfesionalRequest.toUpdateEntity(request, experience);

        if (request.getDocumento() != null && file != null && !file.isEmpty()) {
            if (!tablaMaestraCache.existsItem(tablaMaestraCache.TIPO_DOCUMENTO, request.getDocumento().getCodTipoDocumento()))
                throw new BadRequestException("El tipo de documento seleccionado no es válido.");

            if (file.getSize() > 20 * 1024 * 1024) {
                throw new BadRequestException("El archivo no debe superar los 20 MB");
            }

            if (!"application/pdf".equals(file.getContentType())) {
                throw new BadRequestException("Solo se permiten archivos PDF");
            }

            if (experience.getDocumento() != null) {
                String rutaAnterior = experience.getDocumento().getUrl();

                String path = fileStorageService.savePdf(file, request.getDocenteId());

                DocumentoRequest.toUpdateEntity(request.getDocumento(), experience.getDocumento());
                experience.getDocumento().setUrl(path);

                if (rutaAnterior != null) {
                    try {
                        Path pathDelete = Paths.get(rutaAnterior);
                        Files.deleteIfExists(pathDelete);
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
            } else {
                String path = fileStorageService.savePdf(file, request.getDocenteId());

                DocumentoEntity document = DocumentoRequest.toEntity.apply(request.getDocumento());
                document.setUrl(path);
                document.setDocente(experience.getDocente());

                experience.setDocumento(document);
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

            String rutaAnterior = experiencia.getDocumento().getUrl();
            if (rutaAnterior != null) {
                try {
                    Path pathDelete = Paths.get(rutaAnterior);
                    Files.deleteIfExists(pathDelete);
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }

        experiencia.setEliminado(true);
        experiencia.setActualizadoPor(SecurityUtil.getCurrentUserId());
        experiencia.setFechaActualizacion(LocalDateTime.now());

        ExperienciaProfesionalEntity deleted = experienciaProfesionalRepository.save(experiencia);

        return ExperienciaProfesionalResponse.toResponse.apply(deleted);
    }

    @Override
    public Path getPdfPathById(Long id) {
        ExperienciaProfesionalEntity entity = getWithDocumentById(id);

        if (entity.getDocumento() == null) return null;
        if (entity.getDocumento().getUrl() == null) return null;

        return Paths.get(entity.getDocumento().getUrl());
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
