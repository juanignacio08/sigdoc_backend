package com.sigdoc.sigdoc_backend.persistence.repository.maestro;

import com.sigdoc.sigdoc_backend.entity.maestro.DepartamentoAcademicoEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartamentoAcademicoRepository extends JpaRepository<DepartamentoAcademicoEntity, Long> {

    Optional<DepartamentoAcademicoEntity> findByDepartamentoAcademicoIdAndEliminado(Long id, boolean deleted);
    default Optional<DepartamentoAcademicoEntity> getByID(Long id) {
        return findByDepartamentoAcademicoIdAndEliminado(id, false);
    }

    @EntityGraph(attributePaths = {"facultad"})
    Optional<DepartamentoAcademicoEntity> findByDepartamentoAcademicoIdAndEliminadoEquals(Long id, boolean deleted);
    default Optional<DepartamentoAcademicoEntity> getDetailsByID(Long id) {
        return findByDepartamentoAcademicoIdAndEliminadoEquals(id, false);
    }

    List<DepartamentoAcademicoEntity> findByFacultad_FacultadIdAndEliminadoOrderByDenominacionAsc(
            Long facultyId,
            boolean deleted
    );
    default List<DepartamentoAcademicoEntity> getByFaculty(Long facultyId) {
        return findByFacultad_FacultadIdAndEliminadoOrderByDenominacionAsc(facultyId, false);
    }

    List<DepartamentoAcademicoEntity> findByEstadoAndEliminadoOrderByDenominacionAsc(Integer status, boolean deleted);
    default List<DepartamentoAcademicoEntity> getAll() {
        return findByEstadoAndEliminadoOrderByDenominacionAsc(1, false);
    }

}
