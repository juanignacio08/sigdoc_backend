package com.sigdoc.sigdoc_backend.controller.maestro;

import com.sigdoc.sigdoc_backend.expose.request.maestro.EscuelaProfesionalRequest;
import com.sigdoc.sigdoc_backend.expose.response.base.ResponseDTO;
import com.sigdoc.sigdoc_backend.expose.response.maestro.EscuelaProfesionalResponse;
import com.sigdoc.sigdoc_backend.expose.util.AppUtil;
import com.sigdoc.sigdoc_backend.service.maestro.EscuelaProfesionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professional-school")
@Validated
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class EscuelaProfesionalController {

    private final EscuelaProfesionalService escuelaProfesionalService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO<Object>> save(@RequestBody @Valid EscuelaProfesionalRequest request) {
        try {
            EscuelaProfesionalResponse response = escuelaProfesionalService.save(request);
            ResponseDTO<Object> responseDTO =
                    AppUtil.build(response, "Escuela Profesional registrado correctamente.");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en save: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO<Object>> update(@RequestBody @Valid EscuelaProfesionalRequest request) {
        try {
            EscuelaProfesionalResponse response = escuelaProfesionalService.update(request);
            ResponseDTO<Object> responseDTO =
                    AppUtil.build(response, "Escuela Profesional actualizada correctamente.");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en update: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getDetail")
    public ResponseEntity<ResponseDTO<Object>> getDetail(@RequestParam(name = "id") Long id) {
        try {
            EscuelaProfesionalResponse response =
                    escuelaProfesionalService.getProfessionalSchoolById(id);
            ResponseDTO<Object> responseDTO =
                    AppUtil.build(response, "Escuela Profesional obtenida correctamente.");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en getDetail: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getByAcademicApartment")
    public ResponseEntity<ResponseDTO<Object>> getByAcademicApartment(@RequestParam(name = "academicApartmentId") Long academicApartmentId) {
        try {
            List<EscuelaProfesionalResponse> response =
                    escuelaProfesionalService.getByAcademicApartment(academicApartmentId);
            ResponseDTO<Object> responseDTO =
                    AppUtil.build(response, "Escuelas profesionales obtenidas correctamente.");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en getByAcademicApartment: {}", e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO<Object>> delete(@RequestParam(name = "id") Long id) {
        try {
            EscuelaProfesionalResponse response =
                    escuelaProfesionalService.delete(id);
            ResponseDTO<Object> responseDTO =
                    AppUtil.build(response, "Escuela Profesional eliminado correctamente.");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en delete: {}", e.getMessage());
            throw e;
        }
    }

}
