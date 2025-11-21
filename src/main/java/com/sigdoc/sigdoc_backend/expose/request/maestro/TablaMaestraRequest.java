package com.sigdoc.sigdoc_backend.expose.request.maestro;

import com.sigdoc.sigdoc_backend.entity.maestro.TablaMaestraEntity;
import com.sigdoc.sigdoc_backend.expose.util.SecurityUtil;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.function.Function;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TablaMaestraRequest {

    @NotBlank(message = "El campo codigoTabla es requerido")
    @Size(min = 3, max = 3, message = "El campo codigoTabla debe tener exactamente 3 caracteres")
    private String codigoTabla;

    @NotBlank(message = "El campo codigoItem es requerido")
    @Size(min = 3, max = 3, message = "El campo codigoItem debe tener exactamente 3 caracteres")
    private String codigoItem;

    @Size(min = 6, max = 6, message = "El campo codigo debe tener exactamente 6 caracteres")
    private String codigo;

    private Integer orden;

    private String abreviatura;

    @NotBlank(message = "El campo denominacion es requerido")
    @Size(max = 100, message = "El campo denominacion debe tener como maximo 100 caracteres")
    private String denominacion;

    @NotNull(message = "El campo esSistema es requerido")
    private Boolean esSistema;

    private Integer estado;

    public static final Function<TablaMaestraRequest, TablaMaestraEntity> toEntity = request -> TablaMaestraEntity.builder()
            .codigoTabla(request.getCodigoTabla())
            .codigoItem(request.getCodigoItem())
            .codigo(request.getCodigoTabla() + request.getCodigoItem())
            .orden(request.getOrden())
            .abreviatura(request.getAbreviatura())
            .denominacion(request.getDenominacion())
            .esSistema(request.getEsSistema())
            .estado(1)
            .actualizadoPor(SecurityUtil.getCurrentUserId())
            .fechaActualizacion(LocalDateTime.now())
            .creadoPor(SecurityUtil.getCurrentUserId())
            .fechaCreacion(LocalDateTime.now())
            .eliminado(false)
            .build();

    public static void toUpdateEntity(TablaMaestraRequest request, TablaMaestraEntity entity) {
        entity.setOrden(request.getOrden());
        entity.setAbreviatura(request.getAbreviatura());
        entity.setDenominacion(request.getDenominacion());
        if (request.getEstado() != null) entity.setEstado(request.getEstado());
        entity.setFechaActualizacion(LocalDateTime.now());
        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
    }

}
