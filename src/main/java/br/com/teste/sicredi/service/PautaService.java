package br.com.teste.sicredi.service;

import br.com.teste.sicredi.domain.Pauta;
import br.com.teste.sicredi.mapper.PautaMapper;
import br.com.teste.sicredi.repository.PautaRepository;
import br.com.teste.sicredi.representation.request.CadastrarPautaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PautaService {

    @Autowired
    private PautaRepository repository;

    @Autowired
    private PautaMapper pautaMapper;

    public void cadastrarPauta(CadastrarPautaRequest request) {
        Pauta pauta = pautaMapper.toDomain(request);

        repository.save(pauta);
    }

    public Optional<Pauta> getById(Integer idPauta){
        return repository.findById(idPauta);
    }
}
