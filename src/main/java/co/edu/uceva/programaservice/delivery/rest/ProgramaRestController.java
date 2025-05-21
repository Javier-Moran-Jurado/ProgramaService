package co.edu.uceva.programaservice.delivery.rest;

import co.edu.uceva.programaservice.domain.exception.*;
import co.edu.uceva.programaservice.domain.model.UsuarioDTO;
import co.edu.uceva.programaservice.domain.repositories.IProgramaRepository;
import co.edu.uceva.programaservice.domain.services.IFacultadClient;
import co.edu.uceva.programaservice.domain.services.IUsuarioClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import co.edu.uceva.programaservice.domain.model.Programa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.edu.uceva.programaservice.domain.services.IProgramaService;

@RestController
@RequestMapping("/api/v1/programa-service")
public class ProgramaRestController {
    // Declaramos como final el servicio para mejorar la inmutabilidad
    private final IProgramaService programaService;
    private final IUsuarioClient usuarioService;
    private final IFacultadClient facultadService;

    // Constantes para los mensajes de respuesta
    private static final String MENSAJE = "mensaje";
    private static final String PROGRAMA = "programa";
    private static final String PROGRAMAS = "programas";
    private static final String USUARIOS = "usuarios";

    // Inyección de dependencia del servicio que proporciona servicios de CRUD
    public ProgramaRestController(IProgramaService programaService, IUsuarioClient usuarioService, IFacultadClient facultadService) {
        this.programaService = programaService;
        this.usuarioService = usuarioService;
        this.facultadService = facultadService;
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

    @GetMapping("/coordinadores")
    public ResponseEntity<Map<String, Object>> getDocentes() {
        ObjectMapper mapper = new ObjectMapper();
        //https://stackoverflow.com/questions/28821715/java-lang-classcastexception-java-util-linkedhashmap-cannot-be-cast-to-com-test
        List<UsuarioDTO> usuarios = mapper.convertValue(usuarioService.getUsuarios().getBody().get(USUARIOS), new TypeReference<List<UsuarioDTO>>(){});
        List<UsuarioDTO> coordinadores = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();

        for(UsuarioDTO usuario : usuarios) {
            if(usuario.getRol().equals("Coordinador")) {
                coordinadores.add(usuario);
            }
        }
        if (coordinadores.isEmpty()) {
            throw new NoHayCoordinadoresException();
        }
        response.put(USUARIOS, coordinadores);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/facultades")
    public ResponseEntity<Map<String, Object>> getFacultades(){
        return facultadService.getFacultades();
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
