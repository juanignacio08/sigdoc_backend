package com.sigdoc.sigdoc_backend.entity.decanatura;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "docenteacademico", schema = "decanatura")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class DocenteAcademicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "docenteacademicoid")
    private Long docenteAcademicoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docenteid", nullable = false)
    private DocenteEntity docente;

    @Column(name = "codgradoacademico", length = 6, nullable = false)
    private String codGradoAcademico;

    @Column(name = "tituloprofesional", length = 200, nullable = false)
    private String tituloProfesional;

    @Column(name = "institucionegreso", length = 200, nullable = false)
    private String institucionEgreso;

    @Column(name = "fechaobtencion")
    private LocalDate fechaObtencion;

    @Column(name = "especialidad", length = 200)
    private String especialidad;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "documentoid")
    private DocumentoEntity documento;

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
