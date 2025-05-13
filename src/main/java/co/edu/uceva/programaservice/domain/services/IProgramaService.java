package co.edu.uceva.programaservice.domain.services;

import co.edu.uceva.programaservice.domain.model.Programa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IProgramaService {
    Programa save(Programa programa);
    void delete(Programa programa);
    Optional<Programa> findById(Long id);
    Programa update(Programa programa);
    List<Programa> findAll();
    Page<Programa> findAll(Pageable pageable);
}
