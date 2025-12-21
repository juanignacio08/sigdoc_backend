package com.sigdoc.sigdoc_backend.controller.maestro;

import com.sigdoc.sigdoc_backend.expose.request.maestro.FacultadRequest;
import com.sigdoc.sigdoc_backend.expose.response.base.ResponseDTO;
import com.sigdoc.sigdoc_backend.expose.response.maestro.FacultadResponse;
import com.sigdoc.sigdoc_backend.expose.util.AppUtil;
import com.sigdoc.sigdoc_backend.service.maestro.FacultadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faculty")
@Validated
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class FacultadController {

    private final FacultadService facultadService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO<Object>> save(@RequestBody @Valid FacultadRequest request) {
        try {
            FacultadResponse response = facultadService.save(request);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Facultad registrada correctamente.");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en save: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO<Object>> update(@RequestBody @Valid FacultadRequest request) {
        try {
            FacultadResponse response = facultadService.update(request);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Facultad actualizada correctamente.");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en update: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getList")
    public ResponseEntity<ResponseDTO<Object>> getList() {
        try {
            List<FacultadResponse> response = facultadService.getList();
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Listado de facultades obtenido correctamente.");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en getList: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getById")
    public ResponseEntity<ResponseDTO<Object>> getById(@RequestParam(name = "id") Long id) {
        try {
            FacultadResponse response = facultadService.getFacultadById(id);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Facultad obtenida correctamente.");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en getById: {}", e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO<Object>> delete(@RequestParam(name = "id") Long id) {
        try {
            FacultadResponse response = facultadService.delete(id);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Facultad eliminada correctamente.");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en delete: {}", e.getMessage());
            throw e;
        }
    }
}
