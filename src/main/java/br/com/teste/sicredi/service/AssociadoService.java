package br.com.teste.sicredi.service;

import br.com.teste.sicredi.domain.Associado;
import br.com.teste.sicredi.repository.AssociadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AssociadoService {

    @Autowired
    private AssociadoRepository repository;

    public Optional<Associado> getById(Integer idAssociado){
        return repository.findById(idAssociado);
    }
}
