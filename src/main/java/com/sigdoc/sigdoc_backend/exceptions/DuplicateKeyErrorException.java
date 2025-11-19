package com.sigdoc.sigdoc_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateKeyErrorException extends RuntimeException {
    public DuplicateKeyErrorException(String message) {
        super(message);
    }
}
