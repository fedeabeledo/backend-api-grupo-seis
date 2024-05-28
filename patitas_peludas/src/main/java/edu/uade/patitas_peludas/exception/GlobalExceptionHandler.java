package edu.uade.patitas_peludas.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDTO> handleConstrainViolationException(ConstraintViolationException e) {
        List<ErrorDetail> validationErrors = new ArrayList<>();

        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            validationErrors.add(new ErrorDetail(violation.getPropertyPath().toString(), violation.getMessage()));
        }

        return ResponseEntity.badRequest().body(new ErrorDTO(HttpStatus.BAD_REQUEST.value(), validationErrors));
    }

    @Getter
    @AllArgsConstructor
    private static class ErrorDetail {
        private String field;
        private String message;
    }

    @Getter
    @AllArgsConstructor
    private static class ErrorDTO {
        private int code;
        private List<ErrorDetail> errors;
    }
}

