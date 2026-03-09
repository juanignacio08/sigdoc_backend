package com.sigdoc.sigdoc_backend.entity.maestro;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "regionid", length = 2, nullable = false)
    private String regionId;

    @Column(name = "region", length = 100, nullable = false)
    private String region;

    @Column(name = "provinciaid", length = 4, nullable = false)
    private String provinciaId;

    @Column(name = "provincia", length = 100, nullable = false)
    private String provincia;

    @Column(name = "ubigeo", length = 6, nullable = false, unique = true)
    private String ubigeo;

    @Column(name = "distrito", length = 100, nullable = false)
    private String distrito;

}
