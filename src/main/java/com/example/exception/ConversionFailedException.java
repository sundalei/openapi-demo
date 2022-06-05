package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ConversionFailedException extends RuntimeException {

    public ConversionFailedException(String message) {
        super(message);
    }
}
