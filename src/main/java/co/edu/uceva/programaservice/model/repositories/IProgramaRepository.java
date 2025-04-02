package co.edu.uceva.programaservice.model.repositories;

import co.edu.uceva.programaservice.model.entities.Programa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProgramaRepository extends JpaRepository<Programa, Long> {
}
