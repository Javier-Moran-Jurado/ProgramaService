package co.edu.uceva.programaservice.domain.exception;

import org.springframework.validation.BindingResult;

public class ValidationException extends RuntimeException {
    public final BindingResult result;
    public ValidationException(BindingResult result) {
        super("Error de validacion de datos.");
        this.result = result;
    }
}