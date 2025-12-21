package com.sigdoc.sigdoc_backend.entity.maestro;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Table(name = "departamentoacademico", schema = "maestros")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class DepartamentoAcademicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "departamentoacademicoid")
    private Long departamentoAcademicoId;

    @Column(name = "denominacion", nullable = false, length = 200)
    private String denominacion;

    @Column(name = "abreviatura", length = 10)
    private String abreviatura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facultadoid", nullable = false)
    private FacultadEntity facultad;

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
