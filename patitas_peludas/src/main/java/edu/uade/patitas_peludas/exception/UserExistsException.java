package edu.uade.patitas_peludas.exception;

public class UserExistsException extends RuntimeException {
    public UserExistsException(String email) {
        super("User with email " + email + " already exists");
    }
}
