package com.sigdoc.sigdoc_backend.persistence.repository.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteAcademicoEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocenteAcademicoRepository extends JpaRepository<DocenteAcademicoEntity, Long> {

    @EntityGraph(attributePaths = {"documento"})
    Optional<DocenteAcademicoEntity> findByDocenteAcademicoIdAndEliminado(Long id, boolean deleted);
    default Optional<DocenteAcademicoEntity> getWithDocumentByID(Long id) {
        return findByDocenteAcademicoIdAndEliminado(id, false);
    }

}
