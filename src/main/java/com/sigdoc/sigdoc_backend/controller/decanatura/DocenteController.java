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
@RequestMapping("/teacher")
@Validated
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class DocenteController {

    private final DocenteService docenteService;

    @PostMapping(
            value = "/save",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ResponseDTO<Object>> save(
            @RequestPart("data") @Valid DocenteRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        try {
            DocenteResponse response = docenteService.save(request, file);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Docente registrado correctamente!");
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
            @RequestPart("data") @Valid DocenteRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        try {
            DocenteResponse response = docenteService.update(request, file);
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

    @GetMapping("/imagen/{id}")
    public ResponseEntity<Resource> getImagen(@PathVariable Long id) {

        Path path = docenteService.getImagePathById(id);

        if (path == null || !Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error al cargar la imagen");
        }

        MediaType mediaType = getMediaType(path);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(resource);
    }

    private MediaType getMediaType(Path path) {
        String fileName = path.getFileName().toString().toLowerCase();

        if (fileName.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        }
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        }

        return MediaType.APPLICATION_OCTET_STREAM;
    }

}
