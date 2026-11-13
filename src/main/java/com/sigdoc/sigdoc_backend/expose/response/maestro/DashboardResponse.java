package com.sigdoc.sigdoc_backend.expose.response.maestro;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardResponse {

    Integer totalTeacher;
    Double teacherPercent;

    Integer totalTeacherInAcademicApartment;
    Double teacherInAcademicApartmentPercent;

    Integer totalTeacherInProfessionalSchool;
    Double teacherInProfessionalSchoolPercent;

    List<ItemGraphPieResponse> academicApartmentList;
    List<ItemGraphPieResponse> professionalSchoolList;

}
