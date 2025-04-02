package co.edu.uceva.programaservice.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Programa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull(message = "Debe indicar si el programa esta activo")
    private boolean activo;
    @NotEmpty(message = "Debe tener descripcion")
    private String descripcion;
    @NotNull(message = "Debe indicar cuanto dura el programa")
    private int duracion;
    @NotNull(message = "Debe estar vinculado a un docente")
    private long idCoordinador;
    @NotNull(message = "Debe estar vinculado a una facultad")
    private long idFacultad;
    @NotEmpty(message = "Debe indicar el nivel academico")
    private String nivelAcademico;
    @NotEmpty(message = "Debe indicar el nombre del programa")
    private String nombre;
    @NotNull(message = "Debe indicar el numero de creditos")
    private Byte numeroCreditos;
    @NotEmpty(message = "Debe indicar el perfil de egreso")
    private String perfilEgreso;
}
