package br.com.teste.sicredi.service;

import br.com.teste.sicredi.domain.Associado;
import br.com.teste.sicredi.mapper.AssociadoMapper;
import br.com.teste.sicredi.repository.AssociadoRepository;
import br.com.teste.sicredi.representation.request.CadastrarAssociadoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AssociadoService {

    @Autowired
    private AssociadoRepository repository;

    @Autowired
    private AssociadoMapper associadoMapper;

    public void cadastrarAssociado(CadastrarAssociadoRequest request) {

        Associado associado = associadoMapper.toDomain(request);

        log.info("Salvando associado criado.");

        repository.save(associado);
    }

    public Optional<Associado> getById(Integer idAssociado) {
        return repository.findById(idAssociado);
    }
}
