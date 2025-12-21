package com.sigdoc.sigdoc_backend.persistence.repository.maestro;

import com.sigdoc.sigdoc_backend.entity.maestro.FacultadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacultadRepository extends JpaRepository<FacultadEntity, Long> {

    Optional<FacultadEntity> findByFacultadIdAndEliminado(Long id, Boolean deleted);
    default Optional<FacultadEntity> getByID(Long id) {
        return findByFacultadIdAndEliminado(id, false);
    }

    List<FacultadEntity> findByEliminadoOrderByDenominacionAsc(Boolean deleted);
    default List<FacultadEntity> getAll() {
        return findByEliminadoOrderByDenominacionAsc(false);
    }

    List<FacultadEntity> findByEstadoAndEliminadoOrderByDenominacionAsc(Integer status, Boolean deleted);
    default List<FacultadEntity> getAllActives() {
        return findByEstadoAndEliminadoOrderByDenominacionAsc(1, false);
    }
}
