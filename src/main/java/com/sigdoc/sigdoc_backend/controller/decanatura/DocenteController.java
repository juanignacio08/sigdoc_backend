package com.sigdoc.sigdoc_backend.controller.decanatura;

import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocenteRequest;
import com.sigdoc.sigdoc_backend.expose.response.base.PaginatedResponseDTO;
import com.sigdoc.sigdoc_backend.expose.response.base.ResponseDTO;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.DocenteResponse;
import com.sigdoc.sigdoc_backend.expose.util.AppUtil;
import com.sigdoc.sigdoc_backend.service.decanatura.DocenteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
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
            if (size < 1 || size > 100) size = 10;

            Page<DocenteResponse> responsePage = docenteService.getAllPaginated(page, size);

            if (page >= responsePage.getTotalPages() && responsePage.getTotalPages() > 0) throw new IllegalArgumentException("El número de página solicitado (" + page + ") está fuera del rango permitido (0 - " + (responsePage.getTotalPages() - 1) + ")");

            PaginatedResponseDTO responseDTO = AppUtil.buildPaginated(responsePage, "Obteniendo una lista de docentes");

            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en getPage: {} ", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getAllByFilters")
    public ResponseEntity<PaginatedResponseDTO> getAllByFilters(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(name = "typeDocument", required = false) @Size(min = 6, max = 6, message = "El codigo del tipo de documento debe de tener 6 caracteres") String typeDocument,
            @RequestParam(name = "documentNumber", required = false) String documentNumber,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "lastName1", required = false) String lastName1,
            @RequestParam(name = "lastName2", required = false) String lastName2,
            @RequestParam(name = "birthdateStart", required = false) @Size(min = 10, max = 10, message = "La fecha debe de estar en formato dd/mm/yyyy") String birthdateStart,
            @RequestParam(name = "birthdateEnd", required = false) @Size(min = 10, max = 10, message = "La fecha debe de estar en formato dd/mm/yyyy") String birthdateEnd,
            @RequestParam(name = "personalEmail", required = false) @Email(message = "Formato incorrecto del email") String personalEmail,
            @RequestParam(name = "institutionalEmail", required = false) @Email(message = "Formato incorrecto del email") String institutionalEmail,
            @RequestParam(name = "gender", required = false) @Size(min = 6, max = 6, message = "El codigo del genero debe de tener 6 caracteres") String gender,
            @RequestParam(name = "ubigeoActual", required = false) @Size(min = 6, max = 6, message = "El ubigeo debe de tener 6 caracteres") String ubigeoActual,
            @RequestParam(name = "nivelActual", required = false) Integer nivelActual,
            @RequestParam(name = "ubigeoProcedencia", required = false) @Size(min = 6, max = 6, message = "El ubigeo debe de tener 6 caracteres") String ubigeoProcedencia,
            @RequestParam(name = "nivelProcedencia", required = false) Integer nivelProcedencia,
            @RequestParam(name = "maritalStatus", required = false) @Size(min = 6, max = 6, message = "El codigo de estado civil debe de tener 6 caracteres") String maritalStatus,
            @RequestParam(name = "status", required = false) Integer status,
            @RequestParam(name = "dateCreationStart", required = false) @Size(min = 10, max = 10, message = "La fecha debe de estar en formato dd/mm/yyyy") String dateCreationStart,
            @RequestParam(name = "dateCreationEnd", required = false) @Size(min = 10, max = 10, message = "La fecha debe de estar en formato dd/mm/yyyy") String dateCreationEnd
    ) {
        try {
            if (page < 0) page = 0;
            if (size < 1 || size > 100) size = 10;

            Page<DocenteResponse> responsePage = docenteService.getByFilters(page, size, typeDocument, documentNumber, name, lastName1, lastName2, birthdateStart, birthdateEnd, personalEmail, institutionalEmail, gender, ubigeoActual, nivelActual, ubigeoProcedencia, nivelProcedencia, maritalStatus, status, dateCreationStart, dateCreationEnd);

            if (page >= responsePage.getTotalPages() && responsePage.getTotalPages() > 0) throw new IllegalArgumentException("El número de página solicitado (" + page + ") está fuera del rango permitido (0 - " + (responsePage.getTotalPages() - 1) + ")");

            PaginatedResponseDTO responseDTO = AppUtil.buildPaginated(responsePage, "Obteniendo una lista de docentes");

            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en getByFilters: {} ", e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO<Object>> deletee(@RequestParam(name = "documentNumber") String documentNumber) {
        try {
            DocenteResponse response = docenteService.delete(documentNumber);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Docente eliminado!");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en delete: {} ", e.getMessage());
            throw e;
        }
    }

}
