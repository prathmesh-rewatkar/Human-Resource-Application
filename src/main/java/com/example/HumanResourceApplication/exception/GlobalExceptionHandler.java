package com.example.HumanResourceApplication.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Validation Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        for (var error : ex.getBindingResult().getAllErrors()) {
            String field = ((FieldError) error).getField();
            errors.put(field, error.getDefaultMessage());
        }

        return new ResponseEntity<>(Map.of(
                "status", 400,
                "errors", errors
        ), HttpStatus.BAD_REQUEST);
    }

    // 2. Database Errors (NOT NULL, FK, UNIQUE)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDatabase(DataIntegrityViolationException ex) {

        String message;

        if (ex.getRootCause() != null) {
            message = ex.getRootCause().getMessage();
        } else {
            message = ex.getMessage();  // fallback
        }

        return new ResponseEntity<>(Map.of(
                "status", 400,
                "message", message
        ), HttpStatus.BAD_REQUEST);
    }

    // 3. Spring Data REST Not Found
    @ExceptionHandler(org.springframework.data.rest.webmvc.ResourceNotFoundException.class)
    public ResponseEntity<?> handleSpringDataRestNotFound(Exception ex) {

        return new ResponseEntity<>(Map.of(
                "status", 404,
                "message", ex.getMessage()
        ), HttpStatus.NOT_FOUND);
    }

    // 4. Custom Not Found
    @ExceptionHandler(com.example.HumanResourceApplication.exception.ResourceNotFoundException.class)
    public ResponseEntity<?> handleCustomNotFound(Exception ex) {

        return new ResponseEntity<>(Map.of(
                "status", 404,
                "message", ex.getMessage()
        ), HttpStatus.NOT_FOUND);
    }

    // 5. Runtime (Business logic errors)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {

        return new ResponseEntity<>(Map.of(
                "status", 400,
                "message", ex.getMessage()
        ), HttpStatus.BAD_REQUEST);
    }

    // 6. Generic fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {

        return new ResponseEntity<>(Map.of(
                "status", 500,
                "message", "Something went wrong"
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(org.springframework.data.rest.core.RepositoryConstraintViolationException.class)
    public ResponseEntity<?> handleRepoValidation(
            org.springframework.data.rest.core.RepositoryConstraintViolationException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getErrors().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            errors.put(field, error.getDefaultMessage());
        });

        return new ResponseEntity<>(Map.of(
                "status", 400,
                "errors", errors
        ), HttpStatus.BAD_REQUEST);
    }

    //7.Invalid Input in URL
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity
                .badRequest()
                .body("Invalid ID format. Please provide a numeric value.");
    }

    // duplicate error status code
    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<Map<String, String>> handleDuplicate(DuplicateEntityException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)      // ← 409
                .body(Map.of("error", ex.getMessage()));
    }

}