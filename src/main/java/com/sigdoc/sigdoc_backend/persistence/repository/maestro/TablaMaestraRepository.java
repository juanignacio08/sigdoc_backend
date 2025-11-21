package com.sigdoc.sigdoc_backend.persistence.repository.maestro;

import com.sigdoc.sigdoc_backend.entity.maestro.TablaMaestraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TablaMaestraRepository extends JpaRepository<TablaMaestraEntity, Long> {

    boolean existsByCodigoAndEliminado(String code, boolean deleted);
    default boolean existsMasterTable(String code) {
        return existsByCodigoAndEliminado(code, false);
    }

    Optional<TablaMaestraEntity> findByCodigoAndEliminado(String code, boolean deleted);
    default Optional<TablaMaestraEntity> getByCode(String code) {
        return findByCodigoAndEliminado(code, false);
    }

    List<TablaMaestraEntity> findByCodigoItemAndEliminado(String codeItem, boolean deleted);
    default List<TablaMaestraEntity> getTables() {
        return findByCodigoItemAndEliminado("000", false);
    }

    List<TablaMaestraEntity> findByCodigoItemAndEsSistemaAndEliminado(String codeItem, boolean isSystem, boolean deleted);
    default List<TablaMaestraEntity> getTablesByIsSystem(boolean isSystem) {
        return findByCodigoItemAndEsSistemaAndEliminado("000", isSystem, false);
    }

    List<TablaMaestraEntity> findByCodigoTablaAndCodigoItemNotAndEliminado(String tableCode, String itemCode, boolean deleted);
    default List<TablaMaestraEntity> getAllItemsByTable(String tableCode) {
        return findByCodigoTablaAndCodigoItemNotAndEliminado(tableCode, "000", false);
    }

    List<TablaMaestraEntity> findByCodigoTablaAndCodigoItemNotAndEstadoAndEliminado(String tableCode, String itemCode, Integer status, boolean deleted);
    default List<TablaMaestraEntity> getAllActivesItemsByTable(String tableCode) {
        return findByCodigoTablaAndCodigoItemNotAndEstadoAndEliminado(tableCode, "000", 1, false);
    }

}
