package com.sigdoc.sigdoc_backend.controller.decanatura;

import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocenteLaboralRequest ;
import com.sigdoc.sigdoc_backend.expose.response.base.PaginatedResponseDTO;
import com.sigdoc.sigdoc_backend.expose.response.base.ResponseDTO;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.DocenteLaboralResponse;
import com.sigdoc.sigdoc_backend.expose.util.AppUtil;
import com.sigdoc.sigdoc_backend.service.decanatura.DocenteLaboralService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/work-teacher")
@Validated
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class DocenteLaboralController {

    private final DocenteLaboralService docenteLaboralService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO<Object>> save(@RequestBody @Valid DocenteLaboralRequest request) {
        try {
            DocenteLaboralResponse response = docenteLaboralService.save(request);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Información laboral registrada correctamente. Los datos del docente han sido guardados.");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en save: {} ", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO<Object>> update(@RequestBody @Valid DocenteLaboralRequest request) {
        try {
            DocenteLaboralResponse response = docenteLaboralService.update(request);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "La información laboral del docente ha sido actualizada correctamente.");
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

            Page<DocenteLaboralResponse> responsePage = docenteLaboralService.getPaginatedByDocente(page, size, documentNumber);

            if (page >= responsePage.getTotalPages() && responsePage.getTotalPages() > 0) throw new IllegalArgumentException("El número de página solicitado (" + page + ") está fuera del rango permitido (0 - " + (responsePage.getTotalPages() - 1) + ")");

            PaginatedResponseDTO responseDTO = AppUtil.buildPaginated(responsePage, "Consultando informacion laboral registrados del docente.");

            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en getByTeacherPage: {} ", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getDetail")
    public ResponseEntity<ResponseDTO<Object>> getDetail(@RequestParam(name = "id") Long id) {
        try {
            DocenteLaboralResponse response = docenteLaboralService.getWorkTeacherDetailById(id);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Información laboral obtenida correctamente.");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en getDetail: {} ", e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO<Object>> delete(@RequestParam(name = "id") Long id) {
        try {
            DocenteLaboralResponse response = docenteLaboralService.delete(id);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "La información laboral del docente ha sido eliminada correctamente.");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en delete: {} ", e.getMessage());
            throw e;
        }
    }
    
}
