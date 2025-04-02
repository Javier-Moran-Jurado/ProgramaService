package co.edu.uceva.programaservice.controllers;

import org.springframework.web.bind.annotation.*;
import co.edu.uceva.programaservice.model.entities.Programa;
import java.util.List;
import co.edu.uceva.programaservice.model.services.IProgramaService;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/v1/programa-service")
public class ProgramaRestController {
    private IProgramaService programaService;

    @Autowired
    public ProgramaRestController(IProgramaService programaService) {
        this.programaService = programaService;
    }

    @GetMapping("/programas")
    public List<Programa> getProgramas() {
        return programaService.findAll();
    }

    @PostMapping("/programas")
    public Programa save(@RequestBody Programa programa) {
        return programaService.save(new Programa());
    }

    @DeleteMapping("/programas")
    public void delete(@RequestBody Programa programa) {
        programaService.delete(programa);
    }

    @PutMapping("/programa")
    public Programa update(@RequestBody Programa programa) {
        return programaService.update(programa);
    }

    @GetMapping("/programa/{id}")
    public Programa findById(@PathVariable Long id) {
        return programaService.findById(id);
    }
}
