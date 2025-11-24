package com.sigdoc.sigdoc_backend.persistence.repository.maestro;

import com.sigdoc.sigdoc_backend.entity.maestro.UbigeoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UbigeoRepository extends JpaRepository<UbigeoEntity, Long> {

    List<UbigeoEntity> findByCodigoProvinciaAndCodigoDistritoAndEstadoAndEliminadoOrderByDenominacion(String provincia, String distrito, Integer estado, boolean eliminado);
    default List<UbigeoEntity> getDepartamentos() {
        return findByCodigoProvinciaAndCodigoDistritoAndEstadoAndEliminadoOrderByDenominacion("00", "00", 1, false);
    }

}
