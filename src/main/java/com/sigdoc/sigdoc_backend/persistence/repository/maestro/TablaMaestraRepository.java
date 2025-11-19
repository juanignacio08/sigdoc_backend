package com.sigdoc.sigdoc_backend.persistence.repository.maestro;

import com.sigdoc.sigdoc_backend.entity.maestro.TablaMaestraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TablaMaestraRepository extends JpaRepository<TablaMaestraEntity, Long> {
}
