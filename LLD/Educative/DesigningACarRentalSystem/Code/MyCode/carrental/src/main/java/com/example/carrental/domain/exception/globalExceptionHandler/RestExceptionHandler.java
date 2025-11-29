package com.example.carrental.domain.exception.globalExceptionHandler;

import com.example.carrental.domain.exception.DomainException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Map<String,Object>> handleDomain(DomainException ex) {
        Map<String,Object> err = Map.of("error", ex.getClass().getSimpleName(), "message", ex.getMessage());
        return ResponseEntity.badRequest().body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handleAll(Exception ex) {
        Map<String,Object> err = Map.of("error", "InternalError", "message", ex.getMessage());
        return ResponseEntity.status(500).body(err);
    }
}
