package co.edu.uceva.programaservice.model.repositories;

import co.edu.uceva.programaservice.model.entities.Programa;
import org.springframework.data.repository.CrudRepository;

public interface IProgramaRepository extends CrudRepository<Programa, Long> {
}
