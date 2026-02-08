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

    @EntityGraph(attributePaths = {"documento", "docente", "departamentoAcademico"})
    Page<DocenteLaboralEntity> findByDocente_DocenteIdAndEliminadoOrderByEstadoDescFechaInicioDesc(Long docenteId, boolean deleted, Pageable pageable);
    default Page<DocenteLaboralEntity> getByDocentePaginated(Long docenteId, Pageable pageable) {
        return findByDocente_DocenteIdAndEliminadoOrderByEstadoDescFechaInicioDesc(docenteId, false, pageable);
    }

    boolean existsByDocente_DocenteIdAndEstadoEqualsAndEliminado(Long teacherId, Integer status, boolean deleted);
    default boolean existsByDocenteId(Long teacherId) {
        return existsByDocente_DocenteIdAndEstadoEqualsAndEliminado(teacherId, 1, false);
    }

    boolean existsByDocenteLaboralIdNotAndDocente_DocenteIdAndEstadoEqualsAndEliminado(Long id, Long teacherId, Integer status, boolean deleted);
    default boolean existsByDocenteId(Long id, Long teacherId) {
        return existsByDocenteLaboralIdNotAndDocente_DocenteIdAndEstadoEqualsAndEliminado(id, teacherId, 1, false);
    }

    boolean existsByCodigoInstitucionalAndEliminado(String code, boolean deleted);
    default boolean existsByInstitutionalCode(String code) {
        return existsByCodigoInstitucionalAndEliminado(code, false);
    }

    boolean existsByEmailInstitucionalIgnoreCaseAndEliminado(String email, boolean deleted);
    default boolean existsByInstitutionalEmail(String institutionalEmail) {
        return existsByEmailInstitucionalIgnoreCaseAndEliminado(institutionalEmail, false);
    }

    boolean existsByDocenteLaboralIdNotAndCodigoInstitucionalAndEliminado(Long id, String code, boolean deleted);
    default boolean existsByInstitutionalCode(Long id, String code) {
        return existsByDocenteLaboralIdNotAndCodigoInstitucionalAndEliminado(id, code, false);
    }

    boolean existsByDocenteLaboralIdNotAndEmailInstitucionalIgnoreCaseAndEliminado(Long id, String email, boolean deleted);
    default boolean existsByInstitutionalEmail(Long id, String institutionalEmail) {
        return existsByDocenteLaboralIdNotAndEmailInstitucionalIgnoreCaseAndEliminado(id, institutionalEmail, false);
    }

}
