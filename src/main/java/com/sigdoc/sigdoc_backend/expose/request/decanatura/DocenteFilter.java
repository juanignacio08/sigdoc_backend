package com.sigdoc.sigdoc_backend.expose.request.decanatura;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record DocenteFilter(
        @Size(min = 6, max = 6, message = "El codigo del tipo de documento debe de tener 6 caracteres")
        String typeDocument,

        String documentNumber,
        String name,
        String lastName1,
        String lastName2,

        @Size(min = 10, max = 10, message = "La fecha debe de estar en formato dd/mm/yyyy")
        String birthdateStart,

        @Size(min = 10, max = 10, message = "La fecha debe de estar en formato dd/mm/yyyy")
        String birthdateEnd,

        @Email(message = "Formato incorrecto del email")
        String personalEmail,

        @Email(message = "Formato incorrecto del email")
        String institutionalEmail,

        @Size(min = 6, max = 6, message = "El codigo del genero debe de tener 6 caracteres")
        String gender,

        @Size(min = 6, max = 6, message = "El ubigeo debe de tener 6 caracteres")
        String ubigeoActual,

        Integer nivelActual,

        @Size(min = 6, max = 6, message = "El ubigeo debe de tener 6 caracteres")
        String ubigeoProcedencia,

        Integer nivelProcedencia,

        @Size(min = 6, max = 6, message = "El codigo de estado civil debe de tener 6 caracteres")
        String maritalStatus,

        Integer status,

        @Size(min = 10, max = 10, message = "La fecha debe de estar en formato dd/mm/yyyy")
        String dateCreationStart,

        @Size(min = 10, max = 10, message = "La fecha debe de estar en formato dd/mm/yyyy")
        String dateCreationEnd,

        // Filtros académicos
        @Size(min = 6, max = 6, message = "El codigo del grado academico debe de tener 6 caracteres")
        String academicGrade,

        String professionalTitle,
        String academicInstitute,
        String academicSpecialty,

        //Filters Language
        @Size(min = 6, max = 6, message = "El codigo del lenguaje debe de tener 6 caracteres")
        String language,

        @Size(min = 6, max = 6, message = "El codigo del nivel del lenguaje debe de tener 6 caracteres")
        String languageLevel,

        String languageInstitute,

        //Filters Experience
        String company,
        String cargo,

        //Filters Institutional Info
        Long departmentAcademicId,
        String institutionalCode,
        String workRegimeCode,
        String categoryCode,
        String levelCode,
        String dedicationTime,
        String startDate,
        String endDate
) {}
