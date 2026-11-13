package com.sigdoc.sigdoc_backend.persistence.repository.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.CapacitacionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CapacitacionRepository extends JpaRepository<CapacitacionEntity, Long> {

    @EntityGraph(attributePaths = {"documento"})
    Optional<CapacitacionEntity> findByCapacitacionIdAndEliminado(Long id, boolean deleted);
    default Optional<CapacitacionEntity> getWithDocumentByID(Long id) {
        return findByCapacitacionIdAndEliminado(id, false);
    }

    @EntityGraph(attributePaths = {"documento", "docente"})
    Optional<CapacitacionEntity> findByCapacitacionIdAndEliminadoEquals(Long id, boolean deleted);
    default Optional<CapacitacionEntity> getDetailByID(Long id) {
        return findByCapacitacionIdAndEliminadoEquals(id, false);
    }

    Page<CapacitacionEntity> findByDocente_DocenteIdAndEliminadoOrderByFechaInicioDescCapacitacionIdDesc(
            Long docenteId,
            boolean deleted,
            Pageable pageable
    );
    default Page<CapacitacionEntity> getByDocentePaginated(Long docenteId, Pageable pageable) {
        return findByDocente_DocenteIdAndEliminadoOrderByFechaInicioDescCapacitacionIdDesc(docenteId, false, pageable);
    }

}
