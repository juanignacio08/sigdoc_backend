package com.sigdoc.sigdoc_backend.service.maestro.impl;

import com.sigdoc.sigdoc_backend.entity.maestro.DepartamentoAcademicoEntity;
import com.sigdoc.sigdoc_backend.entity.maestro.EscuelaProfesionalEntity;
import com.sigdoc.sigdoc_backend.exceptions.BadRequestException;
import com.sigdoc.sigdoc_backend.exceptions.BusinessRuleException;
import com.sigdoc.sigdoc_backend.exceptions.NotFoundException;
import com.sigdoc.sigdoc_backend.expose.request.maestro.EscuelaProfesionalRequest;
import com.sigdoc.sigdoc_backend.expose.response.maestro.EscuelaProfesionalResponse;
import com.sigdoc.sigdoc_backend.expose.util.SecurityUtil;
import com.sigdoc.sigdoc_backend.persistence.repository.maestro.EscuelaProfesionalRepository;
import com.sigdoc.sigdoc_backend.service.maestro.DepartamentoAcademicoService;
import com.sigdoc.sigdoc_backend.service.maestro.EscuelaProfesionalService;
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
public class EscuelaProfesionalServiceImpl implements EscuelaProfesionalService {

    private final EscuelaProfesionalRepository escuelaProfesionalRepository;

    private final DepartamentoAcademicoService departamentoAcademicoService;

    @Override
    @Transactional
    public EscuelaProfesionalResponse save(EscuelaProfesionalRequest request) {
        String context = "saveProfessionalSchool";
        log.info("Registrando una nueva escuela profesional. [ CONTEXTO : {} ]", context);

        DepartamentoAcademicoEntity departamentoAcademico = departamentoAcademicoService.getById(request.getDepartamentoAcademicoId());
        if (departamentoAcademico.getEstado().equals(0)) throw new BusinessRuleException("El departamento académico se encuentra inactiva.");

        EscuelaProfesionalEntity entity = EscuelaProfesionalRequest.toEntity.apply(request);

        entity.setDepartamentoAcademico(departamentoAcademico);

        EscuelaProfesionalEntity saved = escuelaProfesionalRepository.save(entity);

        return EscuelaProfesionalResponse.toResponse.apply(saved);
    }

    @Override
    @Transactional
    public EscuelaProfesionalResponse update(EscuelaProfesionalRequest request) {
        String context = "updateProfessionalSchool";
        log.info("Editando los datos de una Escuela Profesional. [ ESCUELA-PROFESIONAL : {} | CONTEXTO : {} ]", request.getEscuelaProfesionalId(), context);

        EscuelaProfesionalEntity entity = getById(request.getDepartamentoAcademicoId());

        EscuelaProfesionalRequest.toUpdateEntity(request, entity);

        EscuelaProfesionalEntity updated = escuelaProfesionalRepository.save(entity);

        return EscuelaProfesionalResponse.toResponse.apply(updated);
    }

    @Override
    public EscuelaProfesionalResponse getProfessionalSchoolById(Long id) {
        String context = "getProfessionalSchoolById";
        log.info("Obteniendo informacion detallada de una Escuela Profesional. [ ESCUELA-PROFESIONAL : {} | CONTEXTO : {} ]", id, context);

        EscuelaProfesionalEntity entity = escuelaProfesionalRepository.getDetailsByID(id)
                .orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ ESCUELA-PROFESIONAL ]"));

        return EscuelaProfesionalResponse.toResponseDetail.apply(entity);
    }

    @Override
    public List<EscuelaProfesionalResponse> getByAcademicApartment(Long academicApartment) {
        String context = "getByAcademicAparment";
        log.info("Obteniendo todos las escuelas profesional de un departamento academico. [ DEPARTAMENTOACADEMICO : {} | CONTEXTO : {} ]", academicApartment, context);

        List<EscuelaProfesionalEntity> list = escuelaProfesionalRepository.getByAcademicApartment(academicApartment);

        return list.stream().map(EscuelaProfesionalResponse.toResponse).toList();
    }

    @Override
    @Transactional
    public EscuelaProfesionalResponse delete(Long id) {
        String context = "deleteProfessionalSchool";
        log.info("Eliminado una Escuela Profesional. [ ESCUELA-PROFESIONAL : {} | CONTEXTO : {} ]", id, context);

        EscuelaProfesionalEntity entity = getById(id);

        entity.setEliminado(true);
        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());

        EscuelaProfesionalEntity deleted = escuelaProfesionalRepository.save(entity);

        return EscuelaProfesionalResponse.toResponse.apply(deleted);
    }

    @Override
    public EscuelaProfesionalEntity getById(Long id) {
        if (id == null || id < 1) throw new BadRequestException("Id incorrecto. [ ESCUELA-PROFESIONAL ]");

        return escuelaProfesionalRepository.getByID(id)
                .orElseThrow(() -> new NotFoundException("Recurso no encontrado. [ ESCUELA-PROFESIONAL ]"));
    }
}
