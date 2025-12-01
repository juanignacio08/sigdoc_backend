package com.sigdoc.sigdoc_backend.controller.decanatura;

import com.sigdoc.sigdoc_backend.expose.request.decanatura.DocenteRequest;
import com.sigdoc.sigdoc_backend.expose.response.base.ResponseDTO;
import com.sigdoc.sigdoc_backend.expose.response.decanatura.DocenteResponse;
import com.sigdoc.sigdoc_backend.expose.util.AppUtil;
import com.sigdoc.sigdoc_backend.service.decanatura.DocenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
