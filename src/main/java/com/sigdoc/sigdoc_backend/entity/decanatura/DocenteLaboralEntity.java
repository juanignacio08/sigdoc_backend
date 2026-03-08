package com.sigdoc.sigdoc_backend.entity.decanatura;

import com.sigdoc.sigdoc_backend.entity.maestro.DepartamentoAcademicoEntity;
import com.sigdoc.sigdoc_backend.entity.maestro.EscuelaProfesionalEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "docentelaboral", schema = "decanatura")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class DocenteLaboralEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "docentelaboralid")
    private Long docenteLaboralId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docenteid", nullable = false)
    private DocenteEntity docente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamentoid", nullable = false)
    private DepartamentoAcademicoEntity departamentoAcademico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escuelaid", nullable = false)
    private EscuelaProfesionalEntity escuelaProfesional;

    @Column(name = "codigoinstitucional", length = 10)
    private String codigoInstitucional;

    @Column(name = "emailinstitucional", length = 120)
    private String emailInstitucional;

    @Column(name = "codregimenlaboral", length = 6)
    private String codRegimenLaboral;

    @Column(name = "codcategoriadocentenombrado", length = 6)
    private String codCategoriaDocenteNombrado;

    @Column(name = "codniveldocentecontratado", length = 6)
    private String codNivelDocenteContratado;

    @Column(name = "codtiempodedicacion", length = 6)
    private String codTiempoDedicacion;

    @Column(name = "fechainicio")
    private LocalDate fechaInicio;

    @Column(name = "fechafin")
    private LocalDate fechaFin;

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
