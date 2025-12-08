package com.sigdoc.sigdoc_backend.entity.decanatura;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Table(name = "documento", schema = "decanatura")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class DocumentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "documentoid")
    private Long documentoId;

    @Column(name = "denominacion", length = 200, nullable = false)
    private String denominacion;

    @Column(name = "codtipodocumento", length = 6,nullable = false)
    private String codTipoDocumento;

    @Column(name = "url", columnDefinition = "text", nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "docenteid",
            referencedColumnName = "docenteid"
    )
    private DocenteEntity docente;

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
