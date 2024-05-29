package edu.uade.patitas_peludas.exception;

import edu.uade.patitas_peludas.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleProductNotFoundException(ProductNotFoundException e) {
        ErrorResponseDTO error = new ErrorResponseDTO(e.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(NotEnoughStockException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotEnoughStockException(NotEnoughStockException e) {
        ErrorResponseDTO error = new ErrorResponseDTO(e.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvoiceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvoiceNotFoundException(InvoiceNotFoundException e) {
        ErrorResponseDTO error = new ErrorResponseDTO(e.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(UserNotFoundException e) {
        ErrorResponseDTO error = new ErrorResponseDTO(e.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidPaymentMethod.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidPaymentMethod(InvalidPaymentMethod e) {
        ErrorResponseDTO error = new ErrorResponseDTO(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}
