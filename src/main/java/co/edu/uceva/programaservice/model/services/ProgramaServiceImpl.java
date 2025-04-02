package co.edu.uceva.programaservice.model.services;

import org.springframework.stereotype.Service;
import co.edu.uceva.programaservice.model.entities.Programa;
import java.util.List;
import co.edu.uceva.programaservice.model.repositories.IProgramaRepository;

@Service
public class ProgramaServiceImpl implements IProgramaService {
    IProgramaRepository programaRepository;

    public ProgramaServiceImpl(IProgramaRepository programaRepository) {
        this.programaRepository = programaRepository;
    }

    @Override
    public Programa save(Programa programa) {
        return programaRepository.save(programa);
    }

    @Override
    public void delete(Programa programa) {
        programaRepository.delete(programa);
    }

    @Override
    public Programa findById(Long id) {
        return programaRepository.findById(id).orElse(null);
    }

    @Override
    public Programa update(Programa programa) {
        return programaRepository.save(programa);
    }

    @Override
    public List<Programa> findAll() {
        return (List<Programa>) programaRepository.findAll();
    }
}
