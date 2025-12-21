package com.sigdoc.sigdoc_backend.controller.decanatura;

import com.sigdoc.sigdoc_backend.expose.request.decanatura.ExperienciaProfesionalRequest;
import com.sigdoc.sigdoc_backend.expose.response.base.PaginatedResponseDTO;
import com.sigdoc.sigdoc_backend.expose.response.base.ResponseDTO;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.ExperienciaProfesionalResponse;
import com.sigdoc.sigdoc_backend.expose.util.AppUtil;
import com.sigdoc.sigdoc_backend.service.decanatura.ExperienciaProfesionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/experience")
@Validated
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class ExperienciaProfesionalController {

    private final ExperienciaProfesionalService docenteExperienciaProfesionalService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO<Object>> save(@RequestBody @Valid ExperienciaProfesionalRequest request) {
        try {
            ExperienciaProfesionalResponse response = docenteExperienciaProfesionalService.save(request);
            ResponseDTO<Object> responseDTO = AppUtil.build(response,
                    "Experiencia profesional registrada correctamente.");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en save: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO<Object>> update(@RequestBody @Valid ExperienciaProfesionalRequest request) {
        try {
            ExperienciaProfesionalResponse response = docenteExperienciaProfesionalService.update(request);
            ResponseDTO<Object> responseDTO = AppUtil.build(response,
                    "La experiencia profesional del docente ha sido actualizada correctamente.");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en update: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getByTeacherPage")
    public ResponseEntity<PaginatedResponseDTO> getByTeacherPage(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(name = "documentNumber") String documentNumber
    ) {
        try {
            if (page < 0) page = 0;
            if (size < 1 || size > 100) size = 10;

            Page<ExperienciaProfesionalResponse> responsePage =
                    docenteExperienciaProfesionalService.getPaginatedByDocente(page, size, documentNumber);

            if (page >= responsePage.getTotalPages() && responsePage.getTotalPages() > 0)
                throw new IllegalArgumentException("El número de página solicitado (" + page +
                        ") está fuera del rango permitido (0 - " + (responsePage.getTotalPages() - 1) + ")");

            PaginatedResponseDTO responseDTO = AppUtil.buildPaginated(responsePage,
                    "Consultando experiencias profesionales registradas del docente.");

            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en getByTeacherPage: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getDetail")
    public ResponseEntity<ResponseDTO<Object>> getDetail(@RequestParam(name = "id") Long id) {
        try {
            ExperienciaProfesionalResponse response =
                    docenteExperienciaProfesionalService.getProfessionalExperienceDetailById(id);

            ResponseDTO<Object> responseDTO = AppUtil.build(response,
                    "Experiencia profesional obtenida correctamente.");

            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en getDetail: {}", e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO<Object>> delete(@RequestParam(name = "id") Long id) {
        try {
            ExperienciaProfesionalResponse response =
                    docenteExperienciaProfesionalService.delete(id);

            ResponseDTO<Object> responseDTO = AppUtil.build(response,
                    "La experiencia profesional del docente ha sido eliminada correctamente.");

            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en delete: {}", e.getMessage());
            throw e;
        }
    }

}
