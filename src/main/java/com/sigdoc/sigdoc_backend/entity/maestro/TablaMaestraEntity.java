package com.sigdoc.sigdoc_backend.entity.maestro;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tablamaestra", schema = "maestro")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TablaMaestraEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tablamaestraid")
    Long tablaMaestraId;

    @Column(name = "codigotabla", length = 3, nullable = false)
    private String codigoTabla;

    @Column(name = "codigoitem", length = 3, nullable = false)
    private String codigoItem;

    @Column(name = "codigo", length = 6, nullable = false, unique = true)
    private String codigo;

    @Column(name = "orden")
    private Integer orden;

    @Column(name = "abreviatura", length = 6)
    private String abreviatura;

    @Column(name = "denominacion", length = 100, nullable = false)
    private String denominacion;

    @Column(name = "essistema", nullable = false)
    private Boolean esSistema;

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
