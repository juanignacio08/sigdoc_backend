package com.sigdoc.sigdoc_backend.expose.request.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.DocenteEntity;
import com.sigdoc.sigdoc_backend.expose.util.DateConverterUtil;
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
public class DocenteRequest {

    @NotBlank(message = "El tipo de documento es requerido")
    @Size(min = 6, max = 6, message = "El código de tipo de documento debe tener exactamente 6 caracteres")
    private String codTipoDocumento;

    @NotBlank(message = "El número de documento es requerido")
    @Size(max = 20, message = "El número de documento no debe exceder 20 caracteres")
    private String numDocumento;

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 100, message = "El nombre no debe exceder 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido paterno es requerido")
    @Size(max = 100, message = "El apellido paterno no debe exceder 100 caracteres")
    private String apellidoPaterno;

    @NotBlank(message = "El apellido materno es requerido")
    @Size(max = 100, message = "El apellido materno no debe exceder 100 caracteres")
    private String apellidoMaterno;

    private String fechaNacimiento;

    @Email(message = "El email personal no es válido")
    @Size(max = 120)
    private String emailPersonal;

    @Email(message = "El email institucional no es válido")
    @Size(max = 120)
    private String emailInstitucional;

    @Size(min = 6, max = 6, message = "El código de género debe tener exactamente 6 caracteres")
    private String codGenero;

    @Size(max = 250, message = "La dirección no debe exceder 250 caracteres")
    private String direccion;

    @Size(min = 6, max = 6, message = "El código ubigeo actual debe tener exactamente 6 caracteres")
    private String codUbigeoActual;

    @Size(min = 6, max = 6, message = "El código ubigeo procedencia debe tener exactamente 6 caracteres")
    private String codUbigeoProcedencia;

    @Size(min = 6, max = 6, message = "El código estado civil debe tener exactamente 6 caracteres")
    private String codEstadoCivil;

    @Size(max = 20, message = "El teléfono no debe exceder 20 caracteres")
    private String telefono;

    private String fotografia;

    @NotNull(message = "El estado es requerido")
    private Integer estado;

    public static final Function<DocenteRequest, DocenteEntity> toEntity = request -> DocenteEntity.builder()
            .codTipoDocumento(request.getCodTipoDocumento())
            .numDocumento(request.getNumDocumento())
            .nombre(request.getNombre())
            .apellidoPaterno(request.getApellidoPaterno())
            .apellidoMaterno(request.getApellidoMaterno())
            .fechaNacimiento(DateConverterUtil.parseFechaDDMMYYYY(request.getFechaNacimiento()))
            .emailPersonal(request.getEmailPersonal())
            .emailInstitucional(request.getEmailInstitucional())
            .codGenero(request.getCodGenero())
            .direccion(request.getDireccion())
            .codigoUbigeoActual(request.getCodUbigeoActual())
            .codUbigeoProcedencia(request.getCodUbigeoProcedencia())
            .codEstadoCivil(request.getCodEstadoCivil())
            .telefono(request.getTelefono())
            .fotografia(request.getFotografia())
            .estado(1)
            .actualizadoPor(SecurityUtil.getCurrentUserId())
            .fechaActualizacion(LocalDateTime.now())
            .creadoPor(SecurityUtil.getCurrentUserId())
            .fechaCreacion(LocalDateTime.now())
            .eliminado(false)
            .build();


    public static void toUpdateEntity(DocenteRequest request, DocenteEntity entity) {
        entity.setCodTipoDocumento(request.getCodTipoDocumento());
        entity.setNumDocumento(request.getNumDocumento());
        entity.setNombre(request.getNombre());
        entity.setApellidoPaterno(request.getApellidoPaterno());
        entity.setApellidoMaterno(request.getApellidoMaterno());
        entity.setFechaNacimiento(DateConverterUtil.parseFechaDDMMYYYY(request.getFechaNacimiento()));
        entity.setEmailPersonal(request.getEmailPersonal());
        entity.setEmailInstitucional(request.getEmailInstitucional());
        entity.setCodGenero(request.getCodGenero());
        entity.setDireccion(request.getDireccion());
        entity.setCodigoUbigeoActual(request.getCodUbigeoActual());
        entity.setCodUbigeoProcedencia(request.getCodUbigeoProcedencia());
        entity.setCodEstadoCivil(request.getCodEstadoCivil());
        entity.setTelefono(request.getTelefono());
        entity.setFotografia(request.getFotografia());
        if (request.getEstado() != null)
            entity.setEstado(request.getEstado());
        entity.setActualizadoPor(SecurityUtil.getCurrentUserId());
        entity.setFechaActualizacion(LocalDateTime.now());
    }

}
