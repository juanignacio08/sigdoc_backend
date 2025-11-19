package com.sigdoc.sigdoc_backend.expose.util;

import com.sigdoc.sigdoc_backend.expose.response.base.PaginatedResponseDTO;
import com.sigdoc.sigdoc_backend.expose.response.base.ResponseDTO;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import java.util.List;

@Slf4j
@UtilityClass
public class AppUtil {
    private static final String SUCCESS = "success";

    public static ResponseDTO<Object> build(Object result, String message) {
        return ResponseDTO.builder()
                .data(result)
                .status(SUCCESS)
                .code("200")
                .message(message)
                .build();
    }

    public static PaginatedResponseDTO buildPaginated(Page<?> pageable, String message) {
        return PaginatedResponseDTO.builder()
                .data(pageable.getContent())
                .status(SUCCESS)
                .code("200")
                .message(message)
                .currentPage(pageable.getNumber())
                .totalItems(pageable.getTotalElements())
                .totalPages(pageable.getTotalPages())
                .build();
    }

    public static PaginatedResponseDTO buildPaginated(Page<?> pageable, List<?> similarSource) {
        return PaginatedResponseDTO.builder()
                .data(similarSource)
                .status(SUCCESS)
                .code("200")
                .currentPage(pageable.getNumber())
                .totalItems(pageable.getTotalElements())
                .totalPages(pageable.getTotalPages())
                .build();
    }
}
