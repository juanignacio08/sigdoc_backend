package com.sigdoc.sigdoc_backend.entity.decanatura;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "docente", schema = "decanatura")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocenteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "docenteid")
    private Long docenteId;

    @Column(name = "codtipodocumento", length = 6,nullable = false)
    private String codTipoDocumento;

    @Column(name = "numdocumento", length = 20, nullable = false)
    private String numDocumento;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "apellidopaterno", length = 100, nullable = false)
    private String apellidoPaterno;

    @Column(name = "apellidomaterno", length = 100, nullable = false)
    private String apellidoMaterno;

    @Column(name = "fechanacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "emailpersonal", length = 120)
    private String emailPersonal;

    @Column(name = "emailinstitucional", length = 120)
    private String emailInstitucional;

    @Column(name = "codgenero", length = 6)
    private String codGenero;

    @Column(name = "direccion", length = 250)
    private String direccion;

    @Column(name = "codubigeoactual", length = 6)
    private String codigoUbigeoActual;

    @Column(name = "codubigeoprocedencia", length = 6)
    private String codUbigeoProcedencia;

    @Column(name = "codestadocivil", length = 6)
    private String codEstadoCivil;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "fotografia", columnDefinition = "TEXT")
    private String fotografia;

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

    @OneToMany(mappedBy = "docente", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @SQLRestriction("eliminado = false")
    private List<DocenteAcademicoEntity> academicsInfo;

    @OneToMany(mappedBy = "docente", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @SQLRestriction("eliminado = false")
    private List<DocenteIdiomaEntity> idiomas;

    @OneToMany(mappedBy = "docente", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @SQLRestriction("eliminado = false")
    private List<ExperienciaProfesionalEntity> experiences;
}
