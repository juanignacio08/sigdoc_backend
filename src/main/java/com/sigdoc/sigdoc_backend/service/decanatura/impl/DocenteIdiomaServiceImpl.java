package com.sigdoc.sigdoc_backend.service.decanatura.impl;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteEntity;
import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteIdiomaEntity;
import com.sigdoc.sigdoc_backend.entity.decanatura.DocumentoEntity;
import com.sigdoc.sigdoc_backend.exceptions.BadRequestException;
import com.sigdoc.sigdoc_backend.exceptions.NotFoundException;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocenteIdiomaRequest;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocumentoRequest;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.DocenteIdiomaResponse;
import com.sigdoc.sigdoc_backend.expose.util.SecurityUtil;
import com.sigdoc.sigdoc_backend.persistence.repository.decanatura.DocenteIdiomaRepository;
import com.sigdoc.sigdoc_backend.service.cache.TablaMaestraCache;
import com.sigdoc.sigdoc_backend.service.decanatura.DocenteIdiomaService;
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
public class DocenteIdiomaServiceImpl implements DocenteIdiomaService {

    private final DocenteIdiomaRepository docenteIdiomaRepository;

    private final DocenteService docenteService;
    private final TablaMaestraCache tablaMaestraCache;

    @Override
    @Transactional
    public DocenteIdiomaResponse save(DocenteIdiomaRequest request) {
        String context = "saveDocenteIdioma";
        log.info("Registrando idioma de un docente. [ DOCENTE : {} | CONTEXTO : {} ]",
                request.getDocenteId(), context);

        DocenteEntity docente = docenteService.getById(request.getDocenteId());

        if (!tablaMaestraCache.existsItem(tablaMaestraCache.IDIOMA, request.getCodIdioma()))
            throw new BadRequestException("El idioma seleccionado no es válido.");

        if (!tablaMaestraCache.existsItem(tablaMaestraCache.NIVEL_IDIOMA, request.getNivel()))
            throw new BadRequestException("El nivel de idioma ingresado no es válido.");

        DocenteIdiomaEntity entity = DocenteIdiomaRequest.toEntity.apply(request);

        if (request.getDocumento() != null) {
            if (!tablaMaestraCache.existsItem(
                    tablaMaestraCache.TIPO_DOCUMENTO,
                    request.getDocumento().getCodTipoDocumento()
            )) {
                throw new BadRequestException("El tipo de documento no es válido.");
            }

            DocumentoEntity document = DocumentoRequest.toEntity.apply(request.getDocumento());
            document.setDocente(docente);
            entity.setDocumento(document);
        }

        entity.setDocente(docente);

        DocenteIdiomaEntity saved = docenteIdiomaRepository.save(entity);

        return DocenteIdiomaResponse.toResponse.apply(saved);
    }

    @Override
    @Transactional
    public DocenteIdiomaResponse update(DocenteIdiomaRequest request) {
        String context = "updateDocenteIdioma";
        log.info("Actualizando idioma de un docente. [ DOCENTE : {} | IDIOMA-ID : {} | CONTEXTO : {} ]",
                request.getDocenteId(), request.getDocenteIdiomaId(), context);

        DocenteIdiomaEntity idioma = getWithDocumentById(request.getDocenteIdiomaId());

        if (!tablaMaestraCache.existsItem(tablaMaestraCache.IDIOMA, request.getCodIdioma()))
            throw new BadRequestException("El idioma seleccionado no es válido.");

        if (!tablaMaestraCache.existsItem(tablaMaestraCache.NIVEL_IDIOMA, request.getNivel()))
            throw new BadRequestException("El nivel del idioma no es válido.");

        DocenteIdiomaRequest.toUpdateEntity(request, idioma);

        if (request.getDocumento() != null) {
            if (!tablaMaestraCache.existsItem(tablaMaestraCache.TIPO_DOCUMENTO, request.getDocumento().getCodTipoDocumento())) throw new BadRequestException("El tipo de documento no es válido.");

            if (idioma.getDocumento() != null) {
                DocumentoRequest.toUpdateEntity(request.getDocumento(), idioma.getDocumento());
            } else {
                DocumentoEntity document = DocumentoRequest.toEntity.apply(request.getDocumento());
                document.setDocente(idioma.getDocente());
                idioma.setDocumento(document);
            }
        }

        DocenteIdiomaEntity updated = docenteIdiomaRepository.save(idioma);

        return DocenteIdiomaResponse.toResponse.apply(updated);
    }

    @Override
    public Page<DocenteIdiomaResponse> getPaginatedByDocente(int page, int size, String docenteDocumentNumber) {

        String context = "getDocenteIdiomaPaginated";
        log.info("Consultando idiomas de un docente. [ DOCENTE : {} | CONTEXTO : {} ]", docenteDocumentNumber, context);

        DocenteEntity docente = docenteService.getByDocumentNumber(docenteDocumentNumber);

        Pageable pageable = PageRequest.of(page, size);

        Page<DocenteIdiomaEntity> entityPage =
                docenteIdiomaRepository.getByDocentePaginated(docente.getDocenteId(), pageable);

        return entityPage.map(DocenteIdiomaResponse.toResponse);
    }

    @Override
    public DocenteIdiomaResponse getLanguageTeacherDetailById(Long id) {
        String context = "getDetail";
        log.info("Obteniendo a detalle un idioma. [ DOCENTE-IDIOMA : {} | CONTEXTO : {} ]", id, context);

        DocenteIdiomaEntity docenteIdioma = getDetailById(id);

        return DocenteIdiomaResponse.toResponseDetail.apply(docenteIdioma);
    }

    @Override
    @Transactional
    public DocenteIdiomaResponse delete(Long id) {
        String context = "deleteDocenteIdioma";
        log.info("Eliminando idioma de un docente. [ IDIOMA-ID : {} | CONTEXTO : {} ]", id, context);

        DocenteIdiomaEntity idioma = getWithDocumentById(id);

        if (idioma.getDocumento() != null) {
            idioma.getDocumento().setEliminado(true);
            idioma.getDocumento().setActualizadoPor(SecurityUtil.getCurrentUserId());
            idioma.getDocumento().setFechaActualizacion(LocalDateTime.now());
        }

        idioma.setEliminado(true);
        idioma.setActualizadoPor(SecurityUtil.getCurrentUserId());
        idioma.setFechaActualizacion(LocalDateTime.now());

        DocenteIdiomaEntity deleted = docenteIdiomaRepository.save(idioma);

        return DocenteIdiomaResponse.toResponse.apply(deleted);
    }

    @Override
    public DocenteIdiomaEntity getWithDocumentById(Long id) {
        if (id == null || id < 1) throw new BadRequestException("Id incorrecto. [ DOCENTE-IDIOMA ]");

        return docenteIdiomaRepository.getWithDocumentByID(id)
                .orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ DOCENTE-IDIOMA ]"));
    }

    @Override
    public DocenteIdiomaEntity getDetailById(Long id) {
        if (id == null || id < 1) throw new BadRequestException("Id incorrecto. [ DOCENTE-IDIOMA ]");

        return docenteIdiomaRepository.getDetailByID(id)
                .orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ DOCENTE-IDIOMA ]"));
    }
}
