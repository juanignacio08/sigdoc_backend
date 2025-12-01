package com.sigdoc.sigdoc_backend.persistence.repository.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<DocenteEntity, Long> {

    Optional<DocenteEntity> findByNumDocumentoAndEliminado(String documentNumber, boolean deleted);
    default Optional<DocenteEntity> getByDocumentNumber(String documentNumber) {
        return findByNumDocumentoAndEliminado(documentNumber, false);
    }

}
