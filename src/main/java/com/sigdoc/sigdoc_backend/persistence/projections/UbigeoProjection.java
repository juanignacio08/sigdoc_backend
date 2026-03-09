package com.sigdoc.sigdoc_backend.persistence.projections;

public interface UbigeoProjection {
    // Estos nombres deben coincidir con el "AS" de la query
    String getRegionId();    // Busca "regionId"
    String getRegion();      // Busca "region"

    String getProvinciaId(); // Busca "provinciaId"
    String getProvincia();   // Busca "provincia"

    String getUbigeo();      // Busca "ubigeo"
    String getDistrito();    // Busca "distrito"
}