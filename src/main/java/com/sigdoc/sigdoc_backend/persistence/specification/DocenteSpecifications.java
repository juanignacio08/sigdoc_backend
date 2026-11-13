package com.sigdoc.sigdoc_backend.persistence.specification;

import com.sigdoc.sigdoc_backend.entity.decanatura.*;
import com.sigdoc.sigdoc_backend.entity.maestro.DepartamentoAcademicoEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DocenteSpecifications {

    public static Specification<DocenteEntity> notRemoved() {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("eliminado"), false));
    }

    public static Specification<DocenteEntity> status(Integer status) {
        if (status == null) return null;

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("estado"), status);
    }

    public static Specification<DocenteEntity> byDocumentType(String documentType) {
        if (documentType == null) return null;

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("codTipoDocumento"), documentType.trim());
    }

    public static Specification<DocenteEntity> byDocumentNumber(String documentNumber) {
        if (documentNumber == null) return null;

        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("numDocumento"), "%" + documentNumber.trim() + "%");
    }

    public static Specification<DocenteEntity> byName(String name) {
        if (name == null) return null;

        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")), "%" + name.toLowerCase().trim() + "%");
    }

    public static Specification<DocenteEntity> byLastName1(String lastName1) {
        if (lastName1 == null) return null;

        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("apellidoPaterno")), "%" + lastName1.toLowerCase().trim() + "%");
    }

    public static Specification<DocenteEntity> byLastName2(String lastName2) {
        if (lastName2 == null) return null;

        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("apellidoMaterno")), "%" + lastName2.toLowerCase().trim() + "%");
    }

    public static Specification<DocenteEntity> byBirthdateBetween(LocalDate start, LocalDate end) {
        return (root, query, cb) -> {
            if (start != null && end != null) {
                return cb.between(root.get("fechaNacimiento"), start, end);
            } else if (start != null) {
                return cb.greaterThanOrEqualTo(root.get("fechaNacimiento"), start);
            } else if (end != null) {
                return cb.lessThanOrEqualTo(root.get("fechaNacimiento"), end);
            }
            return null;
        };
    }

    public static Specification<DocenteEntity> byPersonalEmail(String personalEmail) {
        if (personalEmail == null) return null;

        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("emailPersonal")), "%" + personalEmail.trim().toLowerCase() + "%");
    }

    public static Specification<DocenteEntity> byInstitutionalEmail(String institutionalEmail) {
        if (institutionalEmail == null) return null;

        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("emailInstitucional")), "%" + institutionalEmail.trim().toLowerCase() + "%");
    }

    public static Specification<DocenteEntity> byGender(String gender) {
        if (gender == null) return null;

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("codGenero"), gender.trim());
    }

    public static Specification<DocenteEntity> byDepartamentActual(String ubigeoActual) {
        if (ubigeoActual == null) return null;

        String departamentCode = ubigeoActual.trim().substring(0, 2);

        return (root, query, cb) -> cb.like(root.get("codigoUbigeoActual"), departamentCode + "%");
    }

    public static Specification<DocenteEntity> byProvinceActual(String ubigeoActual) {
        if (ubigeoActual == null) return null;

        String provinceCode = ubigeoActual.trim().substring(0, 4);

        return (root, query, cb) -> cb.like(root.get("codigoUbigeoActual"), provinceCode + "%");
    }

    public static Specification<DocenteEntity> byUbigeoActual(String ubigeoActual, Integer nivel) {
        if (ubigeoActual == null) return null;
        if (nivel == null) nivel = 3;

        return switch (nivel) {
            case 1 -> byDepartamentActual(ubigeoActual);
            case 2 -> byProvinceActual(ubigeoActual);
            case 3 -> (root, query, cb) -> cb.equal(root.get("codigoUbigeoActual"), ubigeoActual.trim());
            default -> null;
        };
    }

    public static Specification<DocenteEntity> byDepartamentProcedencia(String ubigeo) {
        if (ubigeo == null) return null;

        String departamentCode = ubigeo.trim().substring(0, 2);

        return (root, query, cb) -> cb.like(root.get("codUbigeoProcedencia"), departamentCode + "%");
    }

    public static Specification<DocenteEntity> byProvinceProcedencia(String ubigeo) {
        if (ubigeo == null) return null;

        String provinceCode = ubigeo.trim().substring(0, 4);

        return (root, query, cb) -> cb.like(root.get("codUbigeoProcedencia"), provinceCode + "%");
    }

    public static Specification<DocenteEntity> byUbigeoProcedencia(String ubigeo, Integer nivel) {
        if (ubigeo == null) return null;
        if (nivel == null) nivel = 3;

        return switch (nivel) {
            case 1 -> byDepartamentProcedencia(ubigeo);
            case 2 -> byProvinceProcedencia(ubigeo);
            case 3 -> (root, query, cb) -> cb.equal(root.get("codUbigeoProcedencia"), ubigeo.trim());
            default -> null;
        };
    }

    public static Specification<DocenteEntity> byMaritalStatus(String maritalStatus) {
        if (maritalStatus == null) return null;

        return (root, query, cb) -> cb.equal(root.get("codEstadoCivil"), maritalStatus.trim());
    }

    public static Specification<DocenteEntity> byCreationDateBetween(LocalDate start, LocalDate end) {
        return (root, query, cb) -> {
            LocalDateTime startDT = start != null ? start.atStartOfDay() : null;
            LocalDateTime endDT = end != null ? end.atTime(LocalTime.MAX) : null;

            if (startDT != null && endDT != null) {
                return cb.between(root.get("fechaCreacion"), startDT, endDT);
            } else if (startDT != null) {
                return cb.greaterThanOrEqualTo(root.get("fechaCreacion"), startDT);
            } else if (endDT != null) {
                return cb.lessThanOrEqualTo(root.get("fechaCreacion"), endDT);
            }
            return null;
        };
    }

    public static Specification<DocenteEntity> byInstitutionalFilters(
            Long departmentAcademicId,
            String institutionalCode,
            String institutionalEmail,
            String workRegimeCode,
            String categoryCode,
            String levelCode,
            String dedicationTime,
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (departmentAcademicId == null &&
                institutionalCode == null &&
                institutionalEmail == null &&
                workRegimeCode == null &&
                categoryCode == null &&
                levelCode == null &&
                dedicationTime == null &&
                startDate == null &&
                endDate == null
        ) return null;

        return (root, query, cb) -> {
            if (query != null) query.distinct(true);

            Join<DocenteEntity, DocenteLaboralEntity> join = root.join("institutionalInfo", JoinType.INNER);

            Join<DocenteLaboralEntity, DepartamentoAcademicoEntity> deptJoin =
                    join.join("departamentoAcademico", JoinType.INNER);

            Predicate p = cb.conjunction();

            if (departmentAcademicId != null) {
                p = cb.and(p, cb.equal(deptJoin.get("departamentoAcademicoId"), departmentAcademicId));
            }

            if (institutionalCode != null) {
                p = cb.and(p, cb.like(cb.lower(join.get("codigoInstitucional")), "%" + institutionalCode.toLowerCase().trim() + "%"));
            }

            if (institutionalEmail != null) {
                p = cb.and(p, cb.like(cb.lower(join.get("emailInstitucional")), "%" + institutionalEmail.toLowerCase().trim() + "%"));
            }

            if (workRegimeCode != null) {
                p = cb.and(p, cb.equal(join.get("codRegimenLaboral"), workRegimeCode));
            }

            if (categoryCode != null) {
                p = cb.and(p, cb.equal(join.get("codCategoriaDocenteNombrado"), categoryCode));
            }

            if (levelCode != null) {
                p = cb.and(p, cb.equal(join.get("codNivelDocenteContratado"), levelCode));
            }

            if (dedicationTime != null) {
                p = cb.and(p, cb.equal(join.get("codTiempoDedicacion"), dedicationTime));
            }

            if (startDate != null && endDate != null) {
                p = cb.and(p, cb.between(join.get("fechaInicio"), startDate, endDate));
            } else if (startDate != null) {
                p = cb.and(p, cb.greaterThanOrEqualTo(join.get("fechaInicio"), startDate));
            } else if (endDate != null) {
                p = cb.and(p, cb.lessThanOrEqualTo(join.get("fechaInicio"), endDate));
            }

            return p;
        };
    }

    public static Specification<DocenteEntity> byAcademicFilters(
            String academicGrade,
            String professionalTitle,
            String institute,
            String specialty
    ) {
        if (academicGrade == null && professionalTitle == null && institute == null && specialty == null) return null;

        return (root, query, cb) -> {
            if (query != null) query.distinct(true);

            Join<DocenteEntity, DocenteAcademicoEntity> join = root.join("academicsInfo", JoinType.INNER);

            Predicate p = cb.conjunction();

            if (academicGrade != null) {
                p = cb.and(p, cb.equal(join.get("codGradoAcademico"), academicGrade));
            }

            if (professionalTitle != null) {
                p = cb.and(p, cb.like(cb.lower(join.get("tituloProfesional")), "%" + professionalTitle.toLowerCase().trim() + "%"));
            }

            if (institute != null) {
                p = cb.and(p, cb.like(cb.lower(join.get("institucionEgreso")), "%" + institute.toLowerCase().trim() + "%"));
            }

            if (specialty != null) {
                p = cb.and(p, cb.like(cb.lower(join.get("especialidad")), "%" + specialty.toLowerCase().trim() + "%"));
            }

            return p;
        };
    }

    public static Specification<DocenteEntity> byLanguageFilters(
            String language,
            String level,
            String institute
    ) {
        if (language == null && level == null && institute == null) return null;

        return (root, query, cb) -> {
            if (query != null) query.distinct(true);

            Join<DocenteEntity, DocenteIdiomaEntity> join = root.join("idiomas", JoinType.INNER);

            Predicate p = cb.conjunction();

            if (language != null) {
                p = cb.and(p, cb.equal(join.get("codIdioma"), language));
            }

            if (level != null) {
                p = cb.and(p, cb.equal(join.get("nivel"), level));
            }

            if (institute != null) {
                p = cb.and(p, cb.like(cb.lower(join.get("institucionEgreso")), "%" + institute.toLowerCase().trim() + "%"));
            }

            return p;
        };
    }

    public static Specification<DocenteEntity> byExperienceFilters(
            String company,
            String cargo
    ) {
        if (company == null && cargo == null) return null;

        return (root, query, cb) -> {
            if (query != null) query.distinct(true);

            Join<DocenteEntity, ExperienciaProfesionalEntity> join = root.join("experiences", JoinType.INNER);

            Predicate p = cb.conjunction();

            if (company != null) {
                p = cb.and(p, cb.like(cb.lower(join.get("empresa")), "%" + company.toLowerCase().trim() + "%"));
            }

            if (cargo != null) {
                p = cb.and(p, cb.like(cb.lower(join.get("cargo")), "%" + cargo.toLowerCase().trim() + "%"));
            }

            return p;
        };
    }

}
