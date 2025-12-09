package com.sigdoc.sigdoc_backend.persistence.repository.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteIdiomaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocenteIdiomaRepository extends JpaRepository<DocenteIdiomaEntity, Long> {

    @EntityGraph(attributePaths = {"documento"})
    Optional<DocenteIdiomaEntity> findByDocenteIdiomaIdAndEliminado(Long id, boolean deleted);
    default Optional<DocenteIdiomaEntity> getWithDocumentByID(Long id) {
        return findByDocenteIdiomaIdAndEliminado(id, false);
    }

    @EntityGraph(attributePaths = {"documento", "docente"})
    Optional<DocenteIdiomaEntity> findByDocenteIdiomaIdAndEliminadoEquals(Long id, boolean deleted);
    default Optional<DocenteIdiomaEntity> getDetailByID(Long id) {
        return findByDocenteIdiomaIdAndEliminadoEquals(id, false);
    }

    Page<DocenteIdiomaEntity> findByDocente_DocenteIdAndEliminadoOrderByCodIdiomaDescNivelDesc(
            Long docenteId,
            boolean deleted,
            Pageable pageable
    );
    default Page<DocenteIdiomaEntity> getByDocentePaginated(Long docenteId, Pageable pageable) {
        return findByDocente_DocenteIdAndEliminadoOrderByCodIdiomaDescNivelDesc(docenteId, false, pageable);
    }

}
