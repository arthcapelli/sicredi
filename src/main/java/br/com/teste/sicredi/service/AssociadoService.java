package br.com.teste.sicredi.service;

import br.com.teste.sicredi.domain.Associado;
import br.com.teste.sicredi.mapper.AssociadoMapper;
import br.com.teste.sicredi.repository.AssociadoRepository;
import br.com.teste.sicredi.representation.request.CadastrarAssociadoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AssociadoService {

    @Autowired
    private AssociadoRepository repository;

    @Autowired
    private AssociadoMapper associadoMapper;

    public void cadastrarAssociado(CadastrarAssociadoRequest request) {

        Associado associado = associadoMapper.toDomain(request);

        repository.save(associado);
    }

    public Optional<Associado> getById(Integer idAssociado) {
        return repository.findById(idAssociado);
    }
}
