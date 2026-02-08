package com.sigdoc.sigdoc_backend.controller.decanatura;

import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocenteFilter;
import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocenteRequest;
import com.sigdoc.sigdoc_backend.expose.response.base.PaginatedResponseDTO;
import com.sigdoc.sigdoc_backend.expose.response.base.ResponseDTO;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.DocenteResponse;
import com.sigdoc.sigdoc_backend.expose.util.AppUtil;
import com.sigdoc.sigdoc_backend.service.decanatura.DocenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teacher")
@Validated
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class DocenteController {

    private final DocenteService docenteService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO<Object>> save(@RequestBody @Valid DocenteRequest request) {
        try {
            DocenteResponse response = docenteService.save(request);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Docente registrado correctamente!");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en save: {} ", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO<Object>> update(@RequestBody @Valid DocenteRequest request) {
        try {
            DocenteResponse response = docenteService.update(request);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Datos del docente actualizado correctamente!");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en update: {} ", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getPage")
    public ResponseEntity<PaginatedResponseDTO> getPage(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size
    ) {
        try {
            if (page < 0) page = 0;
            if (size < 1 || size > 100000) size = 10000;

            Page<DocenteResponse> responsePage = docenteService.getAllPaginated(page, size);

            if (page >= responsePage.getTotalPages() && responsePage.getTotalPages() > 0) throw new IllegalArgumentException("El número de página solicitado (" + page + ") está fuera del rango permitido (0 - " + (responsePage.getTotalPages() - 1) + ")");

            PaginatedResponseDTO responseDTO = AppUtil.buildPaginated(responsePage, "Obteniendo una lista de docentes");

            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en getPage: {} ", e.getMessage());
            throw e;
        }
    }

    @PostMapping("/getAllByFilters")
    public ResponseEntity<PaginatedResponseDTO> getAllByFilters(
            @RequestBody @Valid DocenteFilter filter,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size
            ) {
        try {
            if (page < 0) page = 0;
            if (size < 1 || size > 100) size = 10;

            Page<DocenteResponse> responsePage = docenteService.getByFilters(page, size, filter);

            if (page >= responsePage.getTotalPages() && responsePage.getTotalPages() > 0) throw new IllegalArgumentException("El número de página solicitado (" + page + ") está fuera del rango permitido (0 - " + (responsePage.getTotalPages() - 1) + ")");

            PaginatedResponseDTO responseDTO = AppUtil.buildPaginated(responsePage, "Obteniendo una lista de docentes");

            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en getByFilters: {} ", e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO<Object>> delete(@RequestParam(name = "documentNumber") String documentNumber) {
        try {
            DocenteResponse response = docenteService.delete(documentNumber);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Docente eliminado!");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en delete: {} ", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getDetail")
    public ResponseEntity<ResponseDTO<Object>> getDetail(@RequestParam(name = "documentNumber") String documentNumber) {
        try {
            DocenteResponse response = docenteService.getDetail(documentNumber);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Docente eliminado!");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en getDetail: {} ", e.getMessage());
            throw e;
        }
    }

}
