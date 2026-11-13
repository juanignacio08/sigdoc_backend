package com.sigdoc.sigdoc_backend.service.maestro.impl;

import com.sigdoc.sigdoc_backend.entity.maestro.FacultadEntity;
import com.sigdoc.sigdoc_backend.exceptions.BadRequestException;
import com.sigdoc.sigdoc_backend.exceptions.NotFoundException;
import com.sigdoc.sigdoc_backend.expose.request.maestro.FacultadRequest;
import com.sigdoc.sigdoc_backend.expose.response.maestro.FacultadResponse;
import com.sigdoc.sigdoc_backend.expose.util.SecurityUtil;
import com.sigdoc.sigdoc_backend.persistence.repository.maestro.FacultadRepository;
import com.sigdoc.sigdoc_backend.service.maestro.FacultadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
@Service
public class FacultadServiceImpl implements FacultadService {

    private final FacultadRepository facultadRepository;

    @Override
    @Transactional
    public FacultadResponse save(FacultadRequest request) {
        String context = "saveFaculty";
        log.info("Registrando una facultad. [ CONTEXTO : {} ]", context);

        FacultadEntity entity = FacultadRequest.toEntity.apply(request);

        FacultadEntity saved = facultadRepository.save(entity);

        return FacultadResponse.toResponse.apply(saved);
    }

    @Override
    @Transactional
    public FacultadResponse update(FacultadRequest request) {
        String context = "updateFaculty";
        log.info("Actualizando los datos de una facultad. [ FACULTAD : {} | CONTEXTO : {} ]", request.getFacultadId(), context);

        FacultadEntity entity = getById(request.getFacultadId());

        FacultadRequest.toUpdateEntity(request, entity);

        FacultadEntity updated = facultadRepository.save(entity);

        return FacultadResponse.toResponse.apply(updated);
    }

    @Override
    public List<FacultadResponse> getList() {
        String context = "getList";
        log.info("Buscando todas las facultades registradas. [ CONTEXTO : {} ]", context);

        List<FacultadEntity> list = facultadRepository.getAll();

        return list.stream().map(FacultadResponse.toResponse).toList();
    }

    @Override
    public FacultadResponse getFacultadById(Long id) {
        String context = "getById";
        log.info("Obteniendo una facultad. [ FACULTAD : {} | CONTEXTO : {} ]", id, context);

        FacultadEntity entity = getById(id);

        return FacultadResponse.toResponse.apply(entity);
    }

    @Override
    @Transactional
    public FacultadResponse delete(Long id) {
        String context = "deleteFaculty";
        log.info("Eliminando una facultad. [ FACULTAD : {} | CONTEXTO : {} ]", id, context);

        FacultadEntity entity = getById(id);

        entity.setEliminado(true);
        entity.setFechaActualizacion(LocalDateTime.now());
        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());

        FacultadEntity deleted = facultadRepository.save(entity);

        return FacultadResponse.toResponse.apply(deleted);
    }

    @Override
    public FacultadEntity getById(Long id) {
        if (id == null || id < 1) throw new BadRequestException("Id Incorrecto. [ FACULTAD ]");

        return facultadRepository.getByID(id)
                .orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ FACULTAD ]"));
    }
}
