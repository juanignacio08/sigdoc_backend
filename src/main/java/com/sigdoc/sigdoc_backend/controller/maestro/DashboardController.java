package com.sigdoc.sigdoc_backend.controller.maestro;

import com.sigdoc.sigdoc_backend.expose.response.base.ResponseDTO;
import com.sigdoc.sigdoc_backend.expose.response.maestro.DashboardResponse;
import com.sigdoc.sigdoc_backend.expose.util.AppUtil;
import com.sigdoc.sigdoc_backend.service.maestro.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@Validated
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/getMain")
    public ResponseEntity<ResponseDTO<Object>> getMain() {
        try {
            DashboardResponse response =
                    dashboardService.getDashBoard();
            ResponseDTO<Object> responseDTO =
                    AppUtil.build(response, "Dashboard obtenido correctamente.");
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            log.error("Error en getMain: {}", e.getMessage());
            throw e;
        }
    }

}
