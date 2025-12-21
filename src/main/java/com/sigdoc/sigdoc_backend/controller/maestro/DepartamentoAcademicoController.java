package com.sigdoc.sigdoc_backend.controller.maestro;

import com.sigdoc.sigdoc_backend.expose.request.maestro.DepartamentoAcademicoRequest;
import com.sigdoc.sigdoc_backend.expose.response.base.ResponseDTO;
import com.sigdoc.sigdoc_backend.expose.response.maestro.DepartamentoAcademicoResponse;
import com.sigdoc.sigdoc_backend.expose.util.AppUtil;
import com.sigdoc.sigdoc_backend.service.maestro.DepartamentoAcademicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/academic-department")
@Validated
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class DepartamentoAcademicoController {

    private final DepartamentoAcademicoService departamentoAcademicoService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO<Object>> save(@RequestBody @Valid DepartamentoAcademicoRequest request) {
        try {
            DepartamentoAcademicoResponse response = departamentoAcademicoService.save(request);
            ResponseDTO<Object> responseDTO =
                    AppUtil.build(response, "Departamento académico registrado correctamente.");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en save: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO<Object>> update(@RequestBody @Valid DepartamentoAcademicoRequest request) {
        try {
            DepartamentoAcademicoResponse response = departamentoAcademicoService.update(request);
            ResponseDTO<Object> responseDTO =
                    AppUtil.build(response, "Departamento académico actualizado correctamente.");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en update: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getDetail")
    public ResponseEntity<ResponseDTO<Object>> getDetail(@RequestParam(name = "id") Long id) {
        try {
            DepartamentoAcademicoResponse response =
                    departamentoAcademicoService.getAcademicDepartamentById(id);
            ResponseDTO<Object> responseDTO =
                    AppUtil.build(response, "Departamento académico obtenido correctamente.");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en getDetail: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getByFaculty")
    public ResponseEntity<ResponseDTO<Object>> getByFaculty(@RequestParam(name = "facultyId") Long facultyId) {
        try {
            List<DepartamentoAcademicoResponse> response =
                    departamentoAcademicoService.getByFaculty(facultyId);
            ResponseDTO<Object> responseDTO =
                    AppUtil.build(response, "Departamentos académicos obtenidos correctamente.");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en getByFaculty: {}", e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO<Object>> delete(@RequestParam(name = "id") Long id) {
        try {
            DepartamentoAcademicoResponse response =
                    departamentoAcademicoService.delete(id);
            ResponseDTO<Object> responseDTO =
                    AppUtil.build(response, "Departamento académico eliminado correctamente.");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en delete: {}", e.getMessage());
            throw e;
        }
    }

}
