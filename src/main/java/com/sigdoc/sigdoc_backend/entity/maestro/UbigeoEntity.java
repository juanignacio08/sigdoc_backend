package com.sigdoc.sigdoc_backend.entity.maestro;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ubigeo", schema = "maestros")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UbigeoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ubigeoid")
    private Long ubigeoId;

    @Column(name = "codigoubigeo", length = 6, nullable = false, unique = true)
    private String codigoUbigeo;

    @Column(name = "codigoregion", length = 2, nullable = false)
    private String codigoRegion;

    @Column(name = "codigoprovincia", length = 2, nullable = false)
    private String codigoProvincia;

    @Column(name = "codigodistrito", length = 2, nullable = false)
    private String codigoDistrito;

    @Column(name = "denominacion", length = 100, nullable = false)
    private String denominacion;

    @Column(name = "estado", nullable = false)
    private Integer estado;

    @Column(name = "actualizadopor", nullable = false)
    private Long actualizadoPor;

    @Column(name = "fechaactualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @Column(name = "creadopor", nullable = false)
    private Long creadoPor;

    @Column(name = "fechacreacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "eliminado", nullable = false)
    private Boolean eliminado = false;

}
