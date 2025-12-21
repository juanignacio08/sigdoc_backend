package com.sigdoc.sigdoc_backend.persistence.repository.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.ExperienciaProfesionalEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExperienciaProfesionalRepository extends JpaRepository<ExperienciaProfesionalEntity, Long> {

    @EntityGraph(attributePaths = {"documento"})
    Optional<ExperienciaProfesionalEntity> findByExperienciaProfesionalIdAndEliminado(Long id, boolean deleted);
    default Optional<ExperienciaProfesionalEntity> getWithDocumentByID(Long id) {
        return findByExperienciaProfesionalIdAndEliminado(id, false);
    }

    @EntityGraph(attributePaths = {"documento", "docente"})
    Optional<ExperienciaProfesionalEntity> findByExperienciaProfesionalIdAndEliminadoEquals(Long id, boolean deleted);
    default Optional<ExperienciaProfesionalEntity> getDetailByID(Long id) {
        return findByExperienciaProfesionalIdAndEliminadoEquals(id, false);
    }

    Page<ExperienciaProfesionalEntity> findByDocente_DocenteIdAndEliminadoOrderByFechaInicioDescExperienciaProfesionalIdDesc(
            Long docenteId,
            boolean deleted,
            Pageable pageable
    );
    default Page<ExperienciaProfesionalEntity> getByDocentePaginated(Long docenteId, Pageable pageable) {
        return findByDocente_DocenteIdAndEliminadoOrderByFechaInicioDescExperienciaProfesionalIdDesc(docenteId, false, pageable);
    }

}
