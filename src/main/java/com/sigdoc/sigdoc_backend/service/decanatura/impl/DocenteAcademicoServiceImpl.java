package com.sigdoc.sigdoc_backend.service.decanatura.impl;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteAcademicoEntity;
import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteEntity;
import com.sigdoc.sigdoc_backend.entity.decanatura.DocumentoEntity;
import com.sigdoc.sigdoc_backend.exceptions.BadRequestException;
import com.sigdoc.sigdoc_backend.exceptions.BusinessRuleException;
import com.sigdoc.sigdoc_backend.exceptions.NotFoundException;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocenteAcademicoRequest;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocumentoRequest;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.DocenteAcademicoResponse;
import com.sigdoc.sigdoc_backend.expose.util.SecurityUtil;
import com.sigdoc.sigdoc_backend.persistence.repository.decanatura.DocenteAcademicoRepository;
import com.sigdoc.sigdoc_backend.service.cache.TablaMaestraCache;
import com.sigdoc.sigdoc_backend.service.decanatura.DocenteAcademicoService;
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
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
@Service
public class DocenteAcademicoServiceImpl implements DocenteAcademicoService {

    private final DocenteAcademicoRepository docenteAcademicoRepository;

    private final DocenteService docenteService;
    private final FileStorageService fileStorageService;

    private final TablaMaestraCache tablaMaestraCache;

    @Override
    @Transactional
    public DocenteAcademicoResponse save(DocenteAcademicoRequest request, MultipartFile file) {
        String context = "saveDocenteAcademico";
        log.info("Registrando datos academicos de un docente. [ DOCENTE : {} | CONTEXTO : {} ]", request.getDocenteId(), context);

        DocenteEntity docente = docenteService.getById(request.getDocenteId());

        if (!tablaMaestraCache.existsItem(tablaMaestraCache.GRADO_ACADEMICO, request.getCodGradoAcademico())) throw new BadRequestException("El grado académico ingresado no es válido. Por favor, verifique e inténtelo de nuevo.");
        if (docenteAcademicoRepository.verifyExistsAcademicTeacher(request.getDocenteId(), request.getCodGradoAcademico(), normalizarTexto(request.getTituloProfesional()), normalizarTexto(request.getInstitucionEgreso()))) throw new BusinessRuleException("El grado académico ya esta registrado!");

        DocenteAcademicoEntity entity = DocenteAcademicoRequest.toEntity.apply(request);
        entity.setDocente(docente);

        if (request.getDocumento() != null && file != null) {
            if (!tablaMaestraCache.existsItem(tablaMaestraCache.TIPO_DOCUMENTO, request.getDocumento().getCodTipoDocumento())) throw new BadRequestException("El tipo de documento seleccionado no es válido. Verifique la opción elegida y vuelva a intentarlo.");
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

        DocenteAcademicoEntity saved = docenteAcademicoRepository.save(entity);

        return DocenteAcademicoResponse.toResponse.apply(saved);

    }

    @Override
    @Transactional
    public DocenteAcademicoResponse update(DocenteAcademicoRequest request, MultipartFile file) {
        String context = "updateTeacherAcademic";
        log.info("Actualizando los datos academicos de un docente. [ DOCENTE : {} | DOCENTE-ACADEMICO : {} | CONTEXTO : {} ]", request.getDocenteId(), request.getDocenteAcademicoId(), context);

        DocenteAcademicoEntity docenteAcademico = getWithDocumentById(request.getDocenteAcademicoId());

        if (!tablaMaestraCache.existsItem(tablaMaestraCache.GRADO_ACADEMICO, request.getCodGradoAcademico())) throw new BadRequestException("El grado académico ingresado no es válido. Por favor, verifique e inténtelo de nuevo.");

        Optional<DocenteAcademicoEntity> entity = docenteAcademicoRepository.getAcademicTeacher(request.getDocenteId(), request.getCodGradoAcademico(), normalizarTexto(request.getTituloProfesional()), normalizarTexto(request.getInstitucionEgreso()));

        if (entity.isPresent()) {
            if (!entity.get().getDocenteAcademicoId().equals(docenteAcademico.getDocenteAcademicoId())) {
                throw new BusinessRuleException("El Grado Académico ya esta registrado!");
            }
        }

        DocenteAcademicoRequest.toUpdateEntity(request, docenteAcademico);

        if (request.getDocumento() != null && file != null && !file.isEmpty()) {
            if (!tablaMaestraCache.existsItem(tablaMaestraCache.TIPO_DOCUMENTO, request.getDocumento().getCodTipoDocumento())) throw new BadRequestException("El tipo de documento seleccionado no es válido. Verifique la opción elegida y vuelva a intentarlo.");
            if (file.getSize() > 20 * 1024 * 1024) {
                throw new BadRequestException("El archivo no debe superar los 20 MB");
            }

            if (!"application/pdf".equals(file.getContentType())) {
                throw new BadRequestException("Solo se permiten archivos PDF");
            }

            if (docenteAcademico.getDocumento() != null) {
                String rutaAnterior = docenteAcademico.getDocumento().getUrl();

                String path = fileStorageService.savePdf(file, request.getDocenteId());

                DocumentoRequest.toUpdateEntity(request.getDocumento(), docenteAcademico.getDocumento());
                docenteAcademico.getDocumento().setUrl(path);

                if (rutaAnterior != null) {
                    try {
                        Path pathDelete = Paths.get(rutaAnterior);
                        Files.deleteIfExists(pathDelete);
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
            }
            else {
                String path = fileStorageService.savePdf(file, request.getDocenteId());

                DocumentoEntity document = DocumentoRequest.toEntity.apply(request.getDocumento());
                document.setUrl(path);
                document.setDocente(docenteAcademico.getDocente());

                docenteAcademico.setDocumento(document);
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

        return entityPage.map(entity ->
            DocenteAcademicoResponse.toResponseWithMasterTable(entity, tablaMaestraCache)
        );
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

            String rutaAnterior = docenteAcademico.getDocumento().getUrl();
            if (rutaAnterior != null) {
                try {
                    Path pathDelete = Paths.get(rutaAnterior);
                    Files.deleteIfExists(pathDelete);
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }

        docenteAcademico.setEliminado(true);
        docenteAcademico.setActualizadoPor(SecurityUtil.getCurrentUserId());
        docenteAcademico.setFechaActualizacion(LocalDateTime.now());

        DocenteAcademicoEntity deleted = docenteAcademicoRepository.save(docenteAcademico);

        return DocenteAcademicoResponse.toResponse.apply(deleted);
    }

    @Override
    public Path getPdfPathById(Long id) {
        DocenteAcademicoEntity entity = getWithDocumentById(id);

        if (entity.getDocumento().getUrl() == null) return null;

        return Paths.get(entity.getDocumento().getUrl());
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

    private String normalizarTexto(String texto) {
        if (texto == null) return null;

        return texto
                .trim()                 // elimina espacios al inicio y final
                .replaceAll("\\s+", " ") // convierte múltiples espacios en uno solo
                .toLowerCase();         // normaliza mayúsculas/minúsculas
    }
}
