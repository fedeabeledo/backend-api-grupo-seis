package edu.uade.patitas_peludas.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import edu.uade.patitas_peludas.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleProductNotFoundException(ProductNotFoundException e) {
        ErrorResponseDTO error = new ErrorResponseDTO(e.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDTO> handleConstrainViolationException(ConstraintViolationException e) {
        List<ErrorDetail> validationErrors = new ArrayList<>();

        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            validationErrors.add(new ErrorDetail(violation.getPropertyPath().toString(), violation.getMessage()));
        }

        return ResponseEntity.badRequest().body(new ErrorDTO(HttpStatus.BAD_REQUEST.value(), validationErrors));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<Map<String, String>>> processUnmergeException(final MethodArgumentNotValidException ex) {
        List<Map<String, String>> validationErrors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("field", ((FieldError) error).getField());
            errorMap.put("message", error.getDefaultMessage());
            validationErrors.add(errorMap);
        });

        return ResponseEntity.badRequest().body(validationErrors);
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
