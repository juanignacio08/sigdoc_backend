package com.sigdoc.sigdoc_backend.service.util.impl;

import com.sigdoc.sigdoc_backend.exceptions.BadRequestException;
import com.sigdoc.sigdoc_backend.service.util.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    private final Path root = Paths.get("uploads/docentes");

    @Override
    public String savePdf(MultipartFile file, Long teacherId) {
        if (!"application/pdf".equals(file.getContentType())) throw new BadRequestException("Solo se permiten archivos PDF");

        String fileName = UUID.randomUUID() + ".pdf";
        Path dir = root.resolve(teacherId.toString()).resolve("academico");

        try {
            Files.createDirectories(dir);
            Files.copy(file.getInputStream(), dir.resolve(fileName));
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo", e);
        }

        log.info("Se guardo correctamente el documento.");
        return dir.resolve(fileName).toString();
    }
}
