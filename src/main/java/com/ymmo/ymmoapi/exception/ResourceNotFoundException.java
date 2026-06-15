package com.ymmo.ymmoapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class ResourceNotFoundException extends ResponseException {
    public ResourceNotFoundException(String message) {
        super(message, 204);
    }
}
