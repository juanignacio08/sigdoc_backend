package com.sigdoc.sigdoc_backend.controller.decanatura;

import com.sigdoc.sigdoc_backend.exceptions.BadRequestException;
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
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/teacher-academic")
@Validated
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class DocenteAcademicoController {

    private final DocenteAcademicoService docenteAcademicoService;

    @PostMapping(
            value = "/save",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ResponseDTO<Object>> save(
            @RequestPart("data") @Valid DocenteAcademicoRequest request,
            @RequestPart(value = "file", required = false)  MultipartFile file
            ) {
        try {
            DocenteAcademicoResponse response = docenteAcademicoService.save(request, file);

            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Información académica registrada correctamente. Los datos del docente han sido guardados.");

            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en save: {} ", e.getMessage());
            throw e;
        }
    }

    @PutMapping(
            value = "/update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ResponseDTO<Object>> update(
            @RequestPart("data") @Valid DocenteAcademicoRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        try {
            DocenteAcademicoResponse response = docenteAcademicoService.update(request, file);
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

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO<Object>> delete(@RequestParam(name = "id") Long id) {
        try {
            DocenteAcademicoResponse response = docenteAcademicoService.delete(id);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "La información académica del docente ha sido eliminada correctamente.");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en delete: {} ", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/pdf/{id}")
    public ResponseEntity<Resource> getPdf(@PathVariable Long id) {

        Path path = docenteAcademicoService.getPdfPathById(id);

        if (path == null || !Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error al cargar el PDF");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"documento.pdf\"")
                // ⬆️ inline = ver | attachment = descargar
                .body(resource);
    }

}
