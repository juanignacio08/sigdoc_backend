package com.sigdoc.sigdoc_backend.persistence.repository.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteLaboralEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocenteLaboralRepository extends JpaRepository<DocenteLaboralEntity, Long> {
    
    @EntityGraph(attributePaths = {"documento"})
    Optional<DocenteLaboralEntity> findByDocenteLaboralIdAndEliminado(Long id, boolean deleted);
    default Optional<DocenteLaboralEntity> getWithDocumentByID(Long id) {
        return findByDocenteLaboralIdAndEliminado(id, false);
    }

    @EntityGraph(attributePaths = {"documento", "docente", "departamentoAcademico"})
    Optional<DocenteLaboralEntity> findByDocenteLaboralIdAndEliminadoEquals(Long id, boolean deleted);
    default Optional<DocenteLaboralEntity> getDetailByID(Long id) {
        return findByDocenteLaboralIdAndEliminadoEquals(id, false);
    }

    Page<DocenteLaboralEntity> findByDocente_DocenteIdAndEliminadoOrderByFechaInicioDesc(Long docenteId, boolean deleted, Pageable pageable);
    default Page<DocenteLaboralEntity> getByDocentePaginated(Long docenteId, Pageable pageable) {
        return findByDocente_DocenteIdAndEliminadoOrderByFechaInicioDesc(docenteId, false, pageable);
    }
}
