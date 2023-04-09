package br.com.teste.sicredi.service;

import br.com.teste.sicredi.domain.Pauta;
import br.com.teste.sicredi.exception.DataCriacaoException;
import br.com.teste.sicredi.mapper.PautaMapper;
import br.com.teste.sicredi.repository.PautaRepository;
import br.com.teste.sicredi.representation.request.CadastrarPautaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PautaService {

    @Autowired
    private PautaRepository repository;

    @Autowired
    private PautaMapper pautaMapper;

    public void cadastrarPauta(CadastrarPautaRequest request) {

        validaDataCriacao(request);

        Pauta pauta = pautaMapper.toDomain(request);

        repository.save(pauta);
    }

    private void validaDataCriacao(CadastrarPautaRequest request) {

        if (request.getDataCriacao().isBefore(LocalDateTime.now())) {
            throw new DataCriacaoException("Data de criação inválida.");
        }
    }

    public Optional<Pauta> getPautaById(Integer idPauta) {
        return repository.findById(idPauta);
    }
}
