package com.sigdoc.sigdoc_backend.service.util;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String savePdf(MultipartFile file, Long teacherId);
    String saveImage(MultipartFile file);
}
