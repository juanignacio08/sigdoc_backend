package com.sigdoc.sigdoc_backend.service.maestro.impl;

import com.sigdoc.sigdoc_backend.entity.maestro.DepartamentoAcademicoEntity;
import com.sigdoc.sigdoc_backend.entity.maestro.FacultadEntity;
import com.sigdoc.sigdoc_backend.exceptions.BadRequestException;
import com.sigdoc.sigdoc_backend.exceptions.BusinessRuleException;
import com.sigdoc.sigdoc_backend.exceptions.NotFoundException;
import com.sigdoc.sigdoc_backend.expose.request.maestro.DepartamentoAcademicoRequest;
import com.sigdoc.sigdoc_backend.expose.response.maestro.DepartamentoAcademicoResponse;
import com.sigdoc.sigdoc_backend.expose.util.SecurityUtil;
import com.sigdoc.sigdoc_backend.persistence.repository.maestro.DepartamentoAcademicoRepository;
import com.sigdoc.sigdoc_backend.service.maestro.DepartamentoAcademicoService;
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
public class DepartamentoAcademicoServiceImpl implements DepartamentoAcademicoService {

    private final DepartamentoAcademicoRepository departamentoAcademicoRepository;

    private final FacultadService facultadService;

    @Override
    @Transactional
    public DepartamentoAcademicoResponse save(DepartamentoAcademicoRequest request) {
        String context = "saveAcademicDepartment";
        log.info("Registrando un nuevo departamento academico. [ CONTEXTO : {} ]", context);

        FacultadEntity facultad = facultadService.getById(request.getFacultadId());
        if (facultad.getEstado().equals(0)) throw new BusinessRuleException("La facultad se encuentra inactiva.");

        DepartamentoAcademicoEntity entity = DepartamentoAcademicoRequest.toEntity.apply(request);
        entity.setFacultad(facultad);

        DepartamentoAcademicoEntity saved = departamentoAcademicoRepository.save(entity);

        return DepartamentoAcademicoResponse.toResponse.apply(saved);
    }

    @Override
    @Transactional
    public DepartamentoAcademicoResponse update(DepartamentoAcademicoRequest request) {
        String context = "updateAcademicDepartment";
        log.info("Editando los datos de un departamento academico. [ DEPARTAMENTO-ACADEMICO : {} | CONTEXTO : {} ]", request.getDepartamentoAcademicoId(), context);

        DepartamentoAcademicoEntity entity = getById(request.getDepartamentoAcademicoId());

        DepartamentoAcademicoRequest.toUpdateEntity(request, entity);

        DepartamentoAcademicoEntity updated = departamentoAcademicoRepository.save(entity);

        return DepartamentoAcademicoResponse.toResponse.apply(updated);
    }

    @Override
    public DepartamentoAcademicoResponse getAcademicDepartamentById(Long id) {
        String context = "getAcademicDepartmentById";
        log.info("Obteniendo informacion detallada de un departamento academico. [ DEPARTAMENTO-ACADEMICO : {} | CONTEXTO : {} ]", id, context);

        DepartamentoAcademicoEntity entity = departamentoAcademicoRepository.getDetailsByID(id)
                .orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ DEPARTAMENTO-ACADEMICO ]"));

        return DepartamentoAcademicoResponse.toResponseDetail.apply(entity);
    }

    @Override
    public List<DepartamentoAcademicoResponse> getByFaculty(Long faculty) {
        String context = "getByFaculty";
        log.info("Obteniendo todos los departamentos academicos de una facultad. [ FACULTAD : {} | CONTEXTO : {} ]", faculty, context);

        List<DepartamentoAcademicoEntity> list = departamentoAcademicoRepository.getByFaculty(faculty);

        return list.stream().map(DepartamentoAcademicoResponse.toResponse).toList();
    }

    @Override
    public DepartamentoAcademicoResponse delete(Long id) {
        String context = "deleteAcademicDepartment";
        log.info("Eliminado un departamento academico. [ DEPARTAMENTO-ACADEMICO : {} | CONTEXTO : {} ]", id, context);

        DepartamentoAcademicoEntity entity = getById(id);

        entity.setEliminado(true);
        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());

        DepartamentoAcademicoEntity deleted = departamentoAcademicoRepository.save(entity);

        return DepartamentoAcademicoResponse.toResponse.apply(deleted);
    }

    @Override
    public DepartamentoAcademicoEntity getById(Long id) {
        if (id == null || id < 1) throw new BadRequestException("Id incorrecto. [ DEPARTAMENTO-ACADEMICO ]");

        return departamentoAcademicoRepository.getByID(id)
                .orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ DEPARTAMENTO-ACADEMICO ]"));
    }
}
