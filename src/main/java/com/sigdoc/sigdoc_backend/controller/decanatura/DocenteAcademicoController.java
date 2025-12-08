package com.sigdoc.sigdoc_backend.controller.decanatura;

import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocenteAcademicoRequest;
import com.sigdoc.sigdoc_backend.expose.response.base.PaginatedResponseDTO;
import com.sigdoc.sigdoc_backend.expose.response.base.ResponseDTO;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.DocenteAcademicoResponse;
import com.sigdoc.sigdoc_backend.expose.util.AppUtil;
import com.sigdoc.sigdoc_backend.service.decanatura.DocenteAcademicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teacher-academic")
@Validated
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class DocenteAcademicoController {

    private final DocenteAcademicoService docenteAcademicoService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO<Object>> save(@RequestBody @Valid DocenteAcademicoRequest request) {
        try {
            DocenteAcademicoResponse response = docenteAcademicoService.save(request);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Información académica registrada correctamente. Los datos del docente han sido guardados.");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en save: {} ", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO<Object>> update(@RequestBody @Valid DocenteAcademicoRequest request) {
        try {
            DocenteAcademicoResponse response = docenteAcademicoService.update(request);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "La información académica del docente ha sido actualizada correctamente.");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en update: {} ", e.getMessage());
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

            Page<DocenteAcademicoResponse> responsePage = docenteAcademicoService.getPaginatedByDocente(page, size, documentNumber);

            if (page >= responsePage.getTotalPages() && responsePage.getTotalPages() > 0) throw new IllegalArgumentException("El número de página solicitado (" + page + ") está fuera del rango permitido (0 - " + (responsePage.getTotalPages() - 1) + ")");

            PaginatedResponseDTO responseDTO = AppUtil.buildPaginated(responsePage, "Consultando grados académicos registrados del docente.");

            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en getByTeacherPage: {} ", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getDetail")
    public ResponseEntity<ResponseDTO<Object>> getDetail(@RequestParam(name = "id") Long id) {
        try {
            DocenteAcademicoResponse response = docenteAcademicoService.getAcademicTeacherDetailById(id);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Información académica obtenida correctamente.");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en getDetail: {} ", e.getMessage());
            throw e;
        }
    }

}
