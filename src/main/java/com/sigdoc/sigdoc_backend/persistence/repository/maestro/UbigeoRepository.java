package com.sigdoc.sigdoc_backend.persistence.repository.maestro;

import com.sigdoc.sigdoc_backend.entity.maestro.UbigeoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UbigeoRepository extends JpaRepository<UbigeoEntity, Long> {

    List<UbigeoEntity> findByCodigoProvinciaAndCodigoDistritoAndEstadoAndEliminadoOrderByDenominacion(String provincia, String distrito, Integer estado, boolean eliminado);
    default List<UbigeoEntity> getDepartamentos() {
        return findByCodigoProvinciaAndCodigoDistritoAndEstadoAndEliminadoOrderByDenominacion("00", "00", 1, false);
    }

    Optional<UbigeoEntity> findFirstByCodigoRegionAndCodigoProvinciaAndCodigoDistritoAndEstadoAndEliminado(String codeDepartament, String codeProvince, String codeDistrict, Integer status, boolean deleted);
    default Optional<UbigeoEntity> getDepartamentByCode(String codeDepartament) {
        return findFirstByCodigoRegionAndCodigoProvinciaAndCodigoDistritoAndEstadoAndEliminado(codeDepartament, "00", "00", 1, false);
    }
    default Optional<UbigeoEntity> getProvinceByCode(String departamentCode, String provinceCode) {
        return findFirstByCodigoRegionAndCodigoProvinciaAndCodigoDistritoAndEstadoAndEliminado(departamentCode, provinceCode, "00", 1, false);
    }

    Optional<UbigeoEntity> findByCodigoUbigeoAndEstadoAndEliminado(String code, Integer status, boolean deleted);
    default Optional<UbigeoEntity> getByCode(String code) {
        return findByCodigoUbigeoAndEstadoAndEliminado(code, 1, false);
    }

    boolean existsByCodigoRegionAndCodigoProvinciaAndCodigoDistritoAndEstadoAndEliminado(String codeDepartament, String codeProvince, String codeDistrict, Integer status, boolean deleted);
    default boolean existsDepartamentByCode(String codeDepartament) {
        return existsByCodigoRegionAndCodigoProvinciaAndCodigoDistritoAndEstadoAndEliminado(codeDepartament, "00", "00", 1, false);
    }
    default boolean existsProvinceByCode(String departamentCode, String provinceCode) {
        return existsByCodigoRegionAndCodigoProvinciaAndCodigoDistritoAndEstadoAndEliminado(departamentCode, provinceCode, "00", 1, false);
    }

    List<UbigeoEntity> findByCodigoRegionAndCodigoProvinciaNotAndCodigoDistritoAndEstadoAndEliminadoOrderByDenominacion(String departamentCode, String provinceCode, String districtCode, Integer status, boolean deleted);
    default List<UbigeoEntity> getProvinces(String departamentCode) {
        return findByCodigoRegionAndCodigoProvinciaNotAndCodigoDistritoAndEstadoAndEliminadoOrderByDenominacion(departamentCode, "00", "00", 1, false);
    }

    List<UbigeoEntity> findByCodigoRegionAndCodigoProvinciaAndCodigoDistritoNotAndEstadoAndEliminadoOrderByDenominacion(String departamentCode, String provinceCode, String districtCode, Integer status, boolean deleted);
    default List<UbigeoEntity> getDistricts(String departamentCode, String provinceCode) {
        return findByCodigoRegionAndCodigoProvinciaAndCodigoDistritoNotAndEstadoAndEliminadoOrderByDenominacion(departamentCode, provinceCode, "00", 1, false);
    }

}
