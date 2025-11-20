package com.sigdoc.sigdoc_backend.service.maestro.impl;

import com.sigdoc.sigdoc_backend.entity.maestro.TablaMaestraEntity;
import com.sigdoc.sigdoc_backend.exceptions.DuplicateKeyErrorException;
import com.sigdoc.sigdoc_backend.expose.request.maestro.TablaMaestraRequest;
import com.sigdoc.sigdoc_backend.expose.response.maestro.TablaMaestraResponse;
import com.sigdoc.sigdoc_backend.persistence.repository.maestro.TablaMaestraRepository;
import com.sigdoc.sigdoc_backend.service.maestro.TablaMaestraService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class TablaMaestraServiceImpl implements TablaMaestraService {

    private final TablaMaestraRepository tablaMaestraRepository;

    @Override
    @Transactional
    public TablaMaestraResponse save(TablaMaestraRequest request) {
        String context = "saveMasterTable";
        log.info("Registrando un registro de tabla maestra. [ CONTEXTO : {} ]", context);

        TablaMaestraEntity entity = TablaMaestraRequest.toEntity.apply(request);

        if (existsItem(entity.getCodigo())) throw new DuplicateKeyErrorException("Ya existe un registro con ese codigo: " + entity.getCodigo());

        TablaMaestraEntity saved = tablaMaestraRepository.save(entity);

        return TablaMaestraResponse.toResponse.apply(saved);
    }

    @Override
    public boolean existsItem(String code) {
        return tablaMaestraRepository.existsMasterTable(code);
    }
}
