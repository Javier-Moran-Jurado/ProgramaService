package co.edu.uceva.programaservice.domain.exception;

public class NoHayProgramasException extends RuntimeException {
    public NoHayProgramasException() {
        super("No hay programas en la base de datos.");
    }
}