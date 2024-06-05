package edu.uade.patitas_peludas.exception;

public class InvalidPaymentMethod extends RuntimeException {
    public InvalidPaymentMethod(String message) {
        super(message);
    }
}
