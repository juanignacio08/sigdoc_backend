package com.sigdoc.sigdoc_backend.controller.maestro;

import com.sigdoc.sigdoc_backend.expose.response.base.ResponseDTO;
import com.sigdoc.sigdoc_backend.expose.response.maestro.UbigeoResponse;
import com.sigdoc.sigdoc_backend.expose.util.AppUtil;
import com.sigdoc.sigdoc_backend.service.maestro.UbigeoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ubigeo")
@Validated
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class UbigeoController {

    private final UbigeoService ubigeoService;

    @GetMapping("/getDepartamentos")
    public ResponseEntity<ResponseDTO<Object>> getDepartaments() {
        try {
            List<UbigeoResponse> response = ubigeoService.getDepartamentos();
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Obteniendo todos los departamentos.");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en getDepartaments: {} ", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getProvincias")
    public ResponseEntity<ResponseDTO<Object>> getProvinces(@RequestParam(name = "departamentCode") String departamentCode) {
        try {
            List<UbigeoResponse> response = ubigeoService.getProvincias(departamentCode);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Obteniendo todas las provincias de un departamento.");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en getProvinces: {} ", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getDistritos")
    public ResponseEntity<ResponseDTO<Object>> getDistricts(
            @RequestParam(name = "provinceCode") String provinceCode
    ) {
        try {
            List<UbigeoResponse> response = ubigeoService.getDistritos(provinceCode);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Obteniendo todos los distritos de una provincia.");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en getDistricts: {} ", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getDetail")
    public ResponseEntity<ResponseDTO<Object>> getDetail(
            @RequestParam(name = "code") String code
    ) {
        try {
            UbigeoResponse response = ubigeoService.getUbigeo(code);
            ResponseDTO<Object> responseDTO = AppUtil.build(response, "Obteniendo el ubigeo a detalle.");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error en getDetail: {} ", e.getMessage());
            throw e;
        }
    }

}
