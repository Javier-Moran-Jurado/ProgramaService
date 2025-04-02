package co.edu.uceva.programaservice.delivery.rest;

import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import co.edu.uceva.programaservice.domain.model.Programa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.edu.uceva.programaservice.domain.services.IProgramaService;

@RestController
@RequestMapping("/api/v1/programa-service")
public class ProgramaRestController {
    // Declaramos como final el servicio para mejorar la inmutabilidad
    private final IProgramaService programaService;

    private static final String ERROR = "error";
    private static final String MENSAJE = "mensaje";
    private static final String PROGRAMA = "programa";
    private static final String PROGRAMAS = "programas";

    // Inyección de dependencia del servicio que proporciona servicios de CRUD
    public ProgramaRestController(IProgramaService programaService) {
        this.programaService = programaService;
    }

    /**
     * Listar todos los programas.
     */
    @GetMapping("/programas")
    public ResponseEntity<Map<String, Object>> getProgramas() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Programa> programas = programaService.findAll();

            if (programas.isEmpty()) {
                response.put(MENSAJE, "No hay programas en la base de datos.");
                response.put(PROGRAMAS, programas); // para que sea siempre el mismo campo
                return ResponseEntity.status(HttpStatus.OK).body(response); // 200 pero lista vacía
            }

            response.put(PROGRAMAS, programas);
            return ResponseEntity.ok(response);

        } catch (DataAccessException e) {
            response.put(MENSAJE, "Error al consultar la base de datos.");
            response.put(ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Listar programas con paginación.
     */
    @GetMapping("/programa/page/{page}")
    public ResponseEntity<Object> index(@PathVariable Integer page) {
        Map<String, Object> response = new HashMap<>();
        Pageable pageable = PageRequest.of(page, 4);

        try {
            Page<Programa> programas = programaService.findAll(pageable);

            if (programas.isEmpty()) {
                response.put(MENSAJE, "No hay programas en la página solicitada.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            return ResponseEntity.ok(programas);

        } catch (DataAccessException e) {
            response.put(MENSAJE, "Error al consultar la base de datos.");
            response.put(ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (IllegalArgumentException e) {
            response.put(MENSAJE, "Número de página inválido.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Crear un nuevo programa pasando el objeto en el cuerpo de la petición, usando validaciones
     */
    @PostMapping("/programas")
    public ResponseEntity<Map<String, Object>> save(@Valid @RequestBody Programa programa, BindingResult result) {
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .toList();

            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            // Guardar el programa en la base de datos
            Programa nuevoPrograma = programaService.save(programa);

            response.put(MENSAJE, "El programa ha sido creado con éxito!");
            response.put(PROGRAMA, nuevoPrograma);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (DataAccessException e) {
            response.put(MENSAJE, "Error al insertar el programa en la base de datos.");
            response.put(ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    /**
     * Eliminar un programa pasando el objeto en el cuerpo de la petición.
     */
    @DeleteMapping("/programas")
    public ResponseEntity<Map<String, Object>> delete(@RequestBody Programa programa) {
        Map<String, Object> response = new HashMap<>();
        try {
            Programa programaExistente = programaService.findById(programa.getId());
            if (programaExistente == null) {
                response.put(MENSAJE, "El programa ID: " + programa.getId() + " no existe en la base de datos.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            programaService.delete(programa);
            response.put(MENSAJE, "El programa ha sido eliminado con éxito!");
            return ResponseEntity.ok(response);
        } catch (DataAccessException e) {
            response.put(MENSAJE, "Error al eliminar el programa de la base de datos.");
            response.put(ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Actualizar un programa pasando el objeto en el cuerpo de la petición.
     * @param programa: Objeto Programa que se va a actualizar
     */
    @PutMapping("/programas")
    public ResponseEntity<Map<String, Object>> update(@Valid @RequestBody Programa programa, BindingResult result) {
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .toList();

            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            // Verificar si el programa existe antes de actualizar
            if (programaService.findById(programa.getId()) == null) {
                response.put(MENSAJE, "Error: No se pudo editar, el programa ID: " + programa.getId() + " no existe en la base de datos.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Guardar directamente el programa actualizado en la base de datos
            Programa programaActualizado = programaService.save(programa);

            response.put(MENSAJE, "El programa ha sido actualizado con éxito!");
            response.put(PROGRAMA, programaActualizado);
            return ResponseEntity.ok(response);

        } catch (DataAccessException e) {
            response.put(MENSAJE, "Error al actualizar el programa en la base de datos.");
            response.put(ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Obtener un programa por su ID.
     */
    @GetMapping("/programas/{id}")
    public ResponseEntity<Map<String, Object>> findById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Programa programa = programaService.findById(id);

            if (programa == null) {
                response.put(MENSAJE, "El programa ID: " + id + " no existe en la base de datos.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            response.put(MENSAJE, "El programa ha sido actualizado con éxito!");
            response.put(PROGRAMA, programa);
            return ResponseEntity.ok(response);

        } catch (DataAccessException e) {
            response.put(MENSAJE, "Error al consultar la base de datos.");
            response.put(ERROR, e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
