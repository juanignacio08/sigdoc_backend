package com.sigdoc.sigdoc_backend.persistence.repository.decanatura;

import com.sigdoc.sigdoc_backend.entity.decanatura.FacultadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultadRepository extends JpaRepository<FacultadEntity, Long> {
}
