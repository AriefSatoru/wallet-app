package com.nutech.walletapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.nutech.walletapp.model.dtoresponse.WebResponse;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class ErrorController {
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WebResponse<String>> constraintViolationException(ConstraintViolationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(WebResponse.<String>builder().status(102).message(exception.getMessage()).data(null).build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<WebResponse<String>> handleInvalidJson(HttpMessageNotReadableException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(WebResponse.<String>builder().status(102).message("Paramter amount hanya boleh angka dan tidak boleh lebih kecil dari 0").data(null).build());
    }
    
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<String>> apiException(ResponseStatusException exception) {
        int customStatus;

        // Map status code to custom status values using if-else
        if (exception.getStatusCode() == HttpStatus.BAD_REQUEST) {
            customStatus = 102;
        } else if (exception.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            customStatus = 108;
        } else if (exception.getStatusCode() == HttpStatus.FORBIDDEN) {
            customStatus = 107;
        } else if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
            customStatus = 103;
        } else if (exception.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            customStatus = 105;
        } else {
            customStatus = 100; // Default custom status
        }
        return ResponseEntity.status(exception.getStatusCode())
                .body(WebResponse.<String>builder().status(customStatus).message(exception.getReason()).data(null).build());
    } 
}
