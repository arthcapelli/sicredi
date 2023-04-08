package br.com.teste.sicredi.service;

import br.com.teste.sicredi.domain.Sessao;
import br.com.teste.sicredi.exception.DataLimiteException;
import br.com.teste.sicredi.exception.PautaNaoExisteException;
import br.com.teste.sicredi.exception.SessaoException;
import br.com.teste.sicredi.mapper.SessaoMapper;
import br.com.teste.sicredi.repository.SessaoRepository;
import br.com.teste.sicredi.representation.request.AbrirSessaoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static java.util.Optional.ofNullable;

@Service
public class SessaoService {

    @Autowired
    private SessaoRepository repository;

    @Autowired
    private PautaService pautaService;

    @Autowired
    private SessaoMapper sessaoMapper;

    public void criarSessao(AbrirSessaoRequest request) {

        if (verificaSessaoExiste(request)) {
            throw new SessaoException("A sessão dessa pauta já existe.");
        }
        LocalDateTime dataLimite = verificaDataLimite(request.getDataLimite());

        Sessao sessao = pautaService.getById(request.getIdPauta())
                .map(pauta -> sessaoMapper.toDomain(request, dataLimite))
                .orElseThrow(() -> new PautaNaoExisteException("Pauta não existe."));

        repository.save(sessao);
    }

    private LocalDateTime verificaDataLimite(LocalDateTime dataLimite) {
        return ofNullable(dataLimite)
                .map(this::validaDataLimite)
                .orElse(LocalDateTime.now().plusMinutes(1));
    }

    private LocalDateTime validaDataLimite(LocalDateTime request) {
        if (request.isBefore(LocalDateTime.now())) {
            throw new DataLimiteException("Data limite não pode ser antes da data atual.");
        }

        return request;
    }

    private boolean verificaSessaoExiste(AbrirSessaoRequest request) {
        return repository.existsByIdPauta(request.getIdPauta());
    }

    public boolean sessaoEncerrada(Integer idPauta) {
        return ofNullable(repository.findByIdPauta(idPauta))
                .map(sessao -> sessao.getDataLimite().isBefore(LocalDateTime.now()))
                .orElse(false);
    }

    public boolean verificaExisteSessaoParaPauta(Integer idPauta) {
        return repository.existsByIdPauta(idPauta);
    }
}
