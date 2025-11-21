package com.sigdoc.sigdoc_backend.controller.maestro;

import com.sigdoc.sigdoc_backend.expose.request.maestro.TablaMaestraRequest;
import com.sigdoc.sigdoc_backend.expose.response.base.ResponseDTO;
import com.sigdoc.sigdoc_backend.expose.response.maestro.TablaMaestraResponse;
import com.sigdoc.sigdoc_backend.expose.util.AppUtil;
import com.sigdoc.sigdoc_backend.service.maestro.TablaMaestraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/master-table")
@Validated
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class TablaMaestraController {

    private final TablaMaestraService tablaMaestraService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO<Object>> save(@RequestBody @Valid TablaMaestraRequest request) {
        try {
            TablaMaestraResponse response = tablaMaestraService.save(request);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Tabla Maestra registrado correctamente!");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en save: {} ", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO<Object>> update(@RequestBody @Valid TablaMaestraRequest request) {
        try {
            TablaMaestraResponse response = tablaMaestraService.update(request);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Tabla Maestra actualizada!");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en update: {} ", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getAllTables")
    public ResponseEntity<ResponseDTO<Object>> getAllTables() {
        try {
            List<TablaMaestraResponse> response = tablaMaestraService.getAllTables();
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Obteniendo todas las tablas!");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en getAllTables: {} ", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getTablesByIsSystem")
    public ResponseEntity<ResponseDTO<Object>> getTablesByIsSystem(@RequestParam(name = "isSystem") boolean isSystem) {
        try {
            List<TablaMaestraResponse> response = tablaMaestraService.getAllTablesByIsSystem(isSystem);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Obteniendo todas las tablas!");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en getTablesByIsSystem: {} ", e.getMessage());
            throw e;
        }
    }

}
