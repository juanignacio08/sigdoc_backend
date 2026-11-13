package com.sigdoc.sigdoc_backend.persistence.repository.maestro;

import com.sigdoc.sigdoc_backend.entity.maestro.UbigeoEntity;
import com.sigdoc.sigdoc_backend.persistence.projections.UbigeoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UbigeoRepository extends JpaRepository<UbigeoEntity, Long> {

    @Query("SELECT DISTINCT u.regionId AS regionId, u.region AS region " +
            "FROM UbigeoEntity u ORDER BY u.region")
    List<UbigeoProjection> getDepartamentos();

    @Query("SELECT DISTINCT u.regionId AS regionId, u.region AS region, " +
            "u.provinciaId AS provinciaId, u.provincia AS provincia " +
            "FROM UbigeoEntity u " +
            "WHERE u.regionId = :regionid " +
            "ORDER BY u.provincia")
    List<UbigeoProjection> getProvincias(String regionid);

    @Query("SELECT u.regionId AS regionId, u.region AS region, " +
            "u.provinciaId AS provinciaId, u.provincia AS provincia, " +
            "u.ubigeo AS ubigeo, u.distrito AS distrito " +
            "FROM UbigeoEntity u " +
            "WHERE u.provinciaId = :provinciaid " +
            "ORDER BY u.distrito")
    List<UbigeoProjection> getDistritos(String provinciaid);

    Optional<UbigeoEntity> findByUbigeo(String ubigeo);
}