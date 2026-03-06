package com.sigdoc.sigdoc_backend.persistence.repository.maestro;

import com.sigdoc.sigdoc_backend.entity.maestro.EscuelaProfesionalEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EscuelaProfesionalRepository extends JpaRepository<EscuelaProfesionalEntity, Long> {

    Optional<EscuelaProfesionalEntity> findByEscuelaProfesionalIdAndEliminado(Long id, boolean deleted);
    default Optional<EscuelaProfesionalEntity> getByID(Long id) {
        return findByEscuelaProfesionalIdAndEliminado(id, false);
    }

    @EntityGraph(attributePaths = {"departamentoAcademico"})
    Optional<EscuelaProfesionalEntity> findByEscuelaProfesionalIdAndEliminadoEquals(Long id, boolean deleted);
    default Optional<EscuelaProfesionalEntity> getDetailsByID(Long id) {
        return findByEscuelaProfesionalIdAndEliminadoEquals(id, false);
    }

    List<EscuelaProfesionalEntity> findByDepartamentoAcademico_DepartamentoAcademicoIdAndEliminadoOrderByDenominacionAsc(
            Long departamenteAcademicoId,
            boolean deleted
    );
    default List<EscuelaProfesionalEntity> getByAcademicApartment(Long departamenteAcademicoId) {
        return findByDepartamentoAcademico_DepartamentoAcademicoIdAndEliminadoOrderByDenominacionAsc(departamenteAcademicoId, false);
    }

}
