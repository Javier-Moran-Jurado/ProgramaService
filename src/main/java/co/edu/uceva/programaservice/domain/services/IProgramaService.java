package co.edu.uceva.programaservice.domain.services;

import co.edu.uceva.programaservice.domain.model.Programa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProgramaService {
    Programa save(Programa programa);
    void delete(Programa programa);
    Programa findById(Long id);
    Programa update(Programa programa);
    List<Programa> findAll();
    Page<Programa> findAll(Pageable pageable);
}
