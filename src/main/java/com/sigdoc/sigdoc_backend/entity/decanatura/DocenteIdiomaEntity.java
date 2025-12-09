package com.sigdoc.sigdoc_backend.entity.decanatura;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "docenteidioma", schema = "decanatura")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class DocenteIdiomaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "docenteidiomaid")
    private Long docenteIdiomaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docenteid", nullable = false)
    private DocenteEntity docente;

    @Column(name = "codidioma", length = 6, nullable = false)
    private String codIdioma;

    @Column(name = "nivel", length = 6, nullable = false)
    private String nivel;

    @Column(name = "institucion", length = 200, nullable = false)
    private String institucion;

    @Column(name = "fechaobtencion")
    private LocalDate fechaObtencion;

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
