package com.sigdoc.sigdoc_backend.persistence.repository.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<DocenteEntity, Long>, JpaSpecificationExecutor<DocenteEntity> {

    Optional<DocenteEntity> findByNumDocumentoAndEliminado(String documentNumber, boolean deleted);
    default Optional<DocenteEntity> getByDocumentNumber(String documentNumber) {
        return findByNumDocumentoAndEliminado(documentNumber, false);
    }

    Page<DocenteEntity> findByEliminadoOrderByFechaActualizacionDesc(boolean deleted, Pageable pageable);
    default Page<DocenteEntity> getAllPaginated(Pageable pageable) {
        return findByEliminadoOrderByFechaActualizacionDesc(false, pageable);
    }

    Optional<DocenteEntity> findByDocenteIdAndEliminado(Long docenteId, boolean deleted);
    default Optional<DocenteEntity> getByID(Long docenteId) {
        return findByDocenteIdAndEliminado(docenteId, false);
    }

    @EntityGraph(attributePaths = {"academicsInfo"})
    Optional<DocenteEntity> findByNumDocumentoAndEliminadoEquals(String docenteId, boolean deleted);
    default Optional<DocenteEntity> getDetailByID(String docenteId) {
        return findByNumDocumentoAndEliminadoEquals(docenteId, false);
    }

    @Override
    @NonNull
    Page<DocenteEntity> findAll(
            Specification<DocenteEntity> specification,
            @NonNull Pageable pageable
    );

    Long countByEliminadoFalseAndEstadoEquals(Integer estado);

}
