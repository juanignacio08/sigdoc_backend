package com.sigdoc.sigdoc_backend.persistence.repository.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteAcademicoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @EntityGraph(attributePaths = {"documento", "docente"})
    Optional<DocenteAcademicoEntity> findByDocenteAcademicoIdAndEliminadoEquals(Long id, boolean deleted);
    default Optional<DocenteAcademicoEntity> getDetailByID(Long id) {
        return findByDocenteAcademicoIdAndEliminadoEquals(id, false);
    }

    @EntityGraph(attributePaths = {"documento"})
    Page<DocenteAcademicoEntity> findByDocente_DocenteIdAndEliminadoOrderByCodGradoAcademicoDesc(Long docenteId, boolean deleted, Pageable pageable);
    default Page<DocenteAcademicoEntity> getByDocentePaginated(Long docenteId, Pageable pageable) {
        return findByDocente_DocenteIdAndEliminadoOrderByCodGradoAcademicoDesc(docenteId, false, pageable);
    }

    boolean existsByDocente_DocenteIdAndCodGradoAcademicoAndTituloProfesionalIgnoreCaseAndInstitucionEgresoIgnoreCaseAndEliminado(Long teacherId, String academicGradeCode, String profession, String institute, boolean deleted);
    default boolean verifyExistsAcademicTeacher(Long teacherId, String academicGradeCode, String profession, String insitute) {
        return existsByDocente_DocenteIdAndCodGradoAcademicoAndTituloProfesionalIgnoreCaseAndInstitucionEgresoIgnoreCaseAndEliminado(teacherId, academicGradeCode, profession, insitute, false);
    }

    Optional<DocenteAcademicoEntity> findByDocente_DocenteIdAndCodGradoAcademicoAndTituloProfesionalIgnoreCaseAndInstitucionEgresoIgnoreCaseAndEliminado(
            Long teacherId,
            String academicGradeCode,
            String profession,
            String institute,
            boolean deleted
    );
    default Optional<DocenteAcademicoEntity> getAcademicTeacher(
            Long teacherId,
            String academicGradeCode,
            String profession,
            String institute
    ) {
        return findByDocente_DocenteIdAndCodGradoAcademicoAndTituloProfesionalIgnoreCaseAndInstitucionEgresoIgnoreCaseAndEliminado(
                teacherId,
                academicGradeCode,
                profession,
                institute,
                false
        );
    }


}
