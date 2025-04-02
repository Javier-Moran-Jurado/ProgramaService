package co.edu.uceva.programaservice.domain.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import co.edu.uceva.programaservice.domain.model.Programa;
import java.util.List;
import co.edu.uceva.programaservice.domain.repositories.IProgramaRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProgramaServiceImpl implements IProgramaService {
    IProgramaRepository programaRepository;

    public ProgramaServiceImpl(IProgramaRepository programaRepository) {
        this.programaRepository = programaRepository;
    }

    @Override
    @Transactional
    public Programa save(Programa programa) {
        return programaRepository.save(programa);
    }

    @Override
    @Transactional
    public void delete(Programa programa) {
        programaRepository.delete(programa);
    }

    @Override
    @Transactional
    public Programa findById(Long id) {
        return programaRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Programa update(Programa programa) {
        return programaRepository.save(programa);
    }

    @Override
    @Transactional
    public List<Programa> findAll() {
        return programaRepository.findAll();
    }

    @Override
    @Transactional
    public Page<Programa> findAll(Pageable pageable) {
        return programaRepository.findAll(pageable);
    }
}
