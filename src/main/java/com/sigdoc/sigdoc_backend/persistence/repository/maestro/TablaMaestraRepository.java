package com.sigdoc.sigdoc_backend.persistence.repository.maestro;

import com.sigdoc.sigdoc_backend.entity.maestro.TablaMaestraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TablaMaestraRepository extends JpaRepository<TablaMaestraEntity, Long> {

    boolean existsByCodigoAndEliminado(String code, boolean deleted);
    default boolean existsMasterTable(String code) {
        return existsByCodigoAndEliminado(code, false);
    }

    Optional<TablaMaestraEntity> findByCodigoAndEliminado(String code, boolean deleted);
    default Optional<TablaMaestraEntity> getByCode(String code) {
        return findByCodigoAndEliminado(code, false);
    }
}
