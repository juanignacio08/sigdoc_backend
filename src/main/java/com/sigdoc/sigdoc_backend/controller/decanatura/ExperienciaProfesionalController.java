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
@RequestMapping("/experience")
@Validated
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class ExperienciaProfesionalController {

    private final ExperienciaProfesionalService docenteExperienciaProfesionalService;

    @PostMapping(
            value = "/save",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ResponseDTO<Object>> save(
            @RequestPart("data") @Valid ExperienciaProfesionalRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        try {
            ExperienciaProfesionalResponse response = docenteExperienciaProfesionalService.save(request, file);
            ResponseDTO<Object> responseDTO = AppUtil.build(response,
                    "Experiencia profesional registrada correctamente.");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en save: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping(
            value = "/update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ResponseDTO<Object>> update(
            @RequestPart("data") @Valid ExperienciaProfesionalRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        try {
            ExperienciaProfesionalResponse response = docenteExperienciaProfesionalService.update(request, file);
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

    @GetMapping("/pdf/{id}")
    public ResponseEntity<Resource> getPdf(@PathVariable Long id) {

        Path path = docenteExperienciaProfesionalService.getPdfPathById(id);

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
