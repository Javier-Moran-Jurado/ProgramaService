package co.edu.uceva.programaservice.domain.exception;

public class PaginaSinProgramasException extends RuntimeException {
    public PaginaSinProgramasException(int page) {
        super("No hay programas en la página solicitada: " + page);
    }
}