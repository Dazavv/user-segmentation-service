package com.example.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionResolver {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> notFoundException(NotFoundException exception) {
        log.warn("NotFoundException: ", exception);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage(), exception.getCause()));
    }

    @ExceptionHandler(IllegalRequestException.class)
    public ResponseEntity<ErrorMessage> illegalRequestException(IllegalRequestException exception) {
        log.warn("IllegalRequestException: ", exception);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(exception.getMessage()));
    }

}
