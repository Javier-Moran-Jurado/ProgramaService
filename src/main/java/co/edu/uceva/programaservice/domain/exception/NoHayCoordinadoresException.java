package co.edu.uceva.programaservice.domain.exception;

public class NoHayCoordinadoresException extends RuntimeException {
    public NoHayCoordinadoresException() {
        super("No hay coordinadores en la base de datos.");
    }
}