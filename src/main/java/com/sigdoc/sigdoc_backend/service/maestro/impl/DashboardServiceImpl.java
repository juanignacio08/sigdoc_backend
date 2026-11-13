package com.sigdoc.sigdoc_backend.service.maestro.impl;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteLaboralEntity;
import com.sigdoc.sigdoc_backend.entity.maestro.DepartamentoAcademicoEntity;
import com.sigdoc.sigdoc_backend.entity.maestro.EscuelaProfesionalEntity;
import com.sigdoc.sigdoc_backend.expose.response.maestro.DashboardResponse;
import com.sigdoc.sigdoc_backend.expose.response.maestro.ItemGraphPieResponse;
import com.sigdoc.sigdoc_backend.persistence.repository.decanatura.DocenteLaboralRepository;
import com.sigdoc.sigdoc_backend.persistence.repository.decanatura.DocenteRepository;
import com.sigdoc.sigdoc_backend.persistence.repository.maestro.DepartamentoAcademicoRepository;
import com.sigdoc.sigdoc_backend.persistence.repository.maestro.EscuelaProfesionalRepository;
import com.sigdoc.sigdoc_backend.service.maestro.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
@Service
public class DashboardServiceImpl implements DashboardService {

    private final DocenteRepository docenteRepository;
    private final DocenteLaboralRepository docenteLaboralRepository;
    private final EscuelaProfesionalRepository escuelaProfesionalRepository;
    private final DepartamentoAcademicoRepository departamentoAcademicoRepository;

    @Override
    public DashboardResponse getDashBoard() {
        String context = "getDashBoard";
        log.info("Obteniendo informacion para el DashBoard del sistema. [ CONTEXTO : {} ]", context);

        int totalTeachers = docenteRepository.countByEliminadoFalseAndEstadoEquals(1).intValue();
        List<DocenteLaboralEntity> list = docenteLaboralRepository.getAllActives();

        List<EscuelaProfesionalEntity> schoolList = escuelaProfesionalRepository.getAll();
        List<DepartamentoAcademicoEntity> apartmentsList = departamentoAcademicoRepository.getAll();

        Map<Long, Long> conteoDocentesPorEscuela = list.stream()
                .filter(d -> d.getEscuelaProfesional() != null)
                .collect(Collectors.groupingBy(d -> d.getEscuelaProfesional().getEscuelaProfesionalId(), Collectors.counting()));

        Map<Long, Long> conteoDocentesPorDepartamento = list.stream()
                .filter(d -> d.getDepartamentoAcademico() != null)
                .collect(Collectors.groupingBy(d -> d.getDepartamentoAcademico().getDepartamentoAcademicoId(), Collectors.counting()));

        List<ItemGraphPieResponse> professionalSchoolList = schoolList.stream()
                .map(ep -> {
                    ItemGraphPieResponse item = new ItemGraphPieResponse();
                    item.setId(ep.getEscuelaProfesionalId());
                    item.setDenomination(ep.getDenominacion());
                    item.setAbbreviation(ep.getAbreviatura());
                    item.setColor(ep.getColor());
                    item.setImage(ep.getImagen());

                    // Obtenemos el conteo del mapa, si no existe ponemos 0
                    Long count = conteoDocentesPorEscuela.getOrDefault(ep.getEscuelaProfesionalId(), 0L);
                    item.setValue(count.intValue());
                    item.setPercent(calculatePercentage(count.intValue(), totalTeachers));

                    return item;
                })
                .toList();

        List<ItemGraphPieResponse> academicApartmentList = apartmentsList.stream()
                .map(da -> {
                    ItemGraphPieResponse item = new ItemGraphPieResponse();
                    item.setId(da.getDepartamentoAcademicoId());
                    item.setDenomination(da.getDenominacion());
                    item.setAbbreviation(da.getAbreviatura());
                    item.setColor(da.getColor());
                    item.setImage(da.getImagen());

                    // Obtenemos el conteo del mapa, si no existe ponemos 0
                    Long count = conteoDocentesPorDepartamento.getOrDefault(da.getDepartamentoAcademicoId(), 0L);
                    item.setValue(count.intValue());
                    item.setPercent(calculatePercentage(count.intValue(), totalTeachers));

                    return item;
                })
                .toList();

        int totalTeacherInAcademicApartment = list.size();
        Double percentAcademicApartment = calculatePercentage(totalTeacherInAcademicApartment, totalTeachers);

        int totalTeacherInProfessionalSchool = list.stream().filter(
                teacher -> teacher.getEscuelaProfesional() != null
        ).toList().size();
        Double percentProfessionalSchool = calculatePercentage(totalTeacherInProfessionalSchool, totalTeachers);

        return DashboardResponse.builder()
                .totalTeacher(totalTeachers)
                .teacherPercent(calculatePercentage(totalTeachers, totalTeachers))

                .totalTeacherInAcademicApartment(totalTeacherInAcademicApartment)
                .teacherInAcademicApartmentPercent(percentAcademicApartment)

                .totalTeacherInProfessionalSchool(totalTeacherInProfessionalSchool)
                .teacherInProfessionalSchoolPercent(percentProfessionalSchool)

                .academicApartmentList(academicApartmentList)
                .professionalSchoolList(professionalSchoolList)

                .build();
    }

    private Double calculatePercentage(int subtotal, int total) {
        if (total == 0) return 0.0;
        double percentage = (subtotal * 100.0) / total;
        return Math.round(percentage * 100.0) / 100.0;
    }
}
