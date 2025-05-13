package co.edu.uceva.programaservice.delivery.rest;

import co.edu.uceva.programaservice.domain.exception.*;
import jakarta.validation.Valid;
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

    // Constantes para los mensajes de respuesta
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
        List<Programa> programas = programaService.findAll();
        if (programas.isEmpty()) {
            throw new NoHayProgramasException();
        }
        Map<String, Object> response = new HashMap<>();
        response.put(PROGRAMAS, programas);
        return ResponseEntity.ok(response);
    }

    /**
     * Listar programas con paginación.
     */
    @GetMapping("/programa/page/{page}")
    public ResponseEntity<Object> index(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 4);
        Page<Programa> programas = programaService.findAll(pageable);
        if (programas.isEmpty()) {
            throw new PaginaSinProgramasException(page);
        }
        return ResponseEntity.ok(programas);
    }

    /**
     * Crear un nuevo programa pasando el objeto en el cuerpo de la petición, usando validaciones
     */
    @PostMapping("/programas")
    public ResponseEntity<Map<String, Object>> save(@Valid @RequestBody Programa programa, BindingResult result) {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        Map<String, Object> response = new HashMap<>();
        Programa nuevoPrograma = programaService.save(programa);
        response.put(MENSAJE, "El programa ha sido creado con éxito!");
        response.put(PROGRAMA, nuevoPrograma);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    /**
     * Eliminar un programa pasando el objeto en el cuerpo de la petición.
     */
    @DeleteMapping("/programas")
    public ResponseEntity<Map<String, Object>> delete(@RequestBody Programa programa) {
        programaService.findById(programa.getId())
                .orElseThrow(() -> new ProgramaNoEncontradoException(programa.getId()));
        programaService.delete(programa);
        Map<String, Object> response = new HashMap<>();
        response.put(MENSAJE, "El programa ha sido eliminado con éxito!");
        response.put(PROGRAMA, null);
        return ResponseEntity.ok(response);
    }

    /**
     * Actualizar un programa pasando el objeto en el cuerpo de la petición.
     * @param programa: Objeto Programa que se va a actualizar
     */
    @PutMapping("/programas")
    public ResponseEntity<Map<String, Object>> update(@Valid @RequestBody Programa programa, BindingResult result) {
        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
        programaService.findById(programa.getId())
                .orElseThrow(() -> new ProgramaNoEncontradoException(programa.getId()));
        Map<String, Object> response = new HashMap<>();
        Programa programaActualizado = programaService.update(programa);
        response.put(MENSAJE, "El programa ha sido actualizado con éxito!");
        response.put(PROGRAMA, programaActualizado);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener un programa por su ID.
     */
    @GetMapping("/programas/{id}")
    public ResponseEntity<Map<String, Object>> findById(@PathVariable Long id) {
        Programa programa = programaService.findById(id)
                .orElseThrow(() -> new ProgramaNoEncontradoException(id));
        Map<String, Object> response = new HashMap<>();
        response.put(MENSAJE, "El programa ha sido encontrado con éxito!");
        response.put(PROGRAMA, programa);
        return ResponseEntity.ok(response);
    }
}
