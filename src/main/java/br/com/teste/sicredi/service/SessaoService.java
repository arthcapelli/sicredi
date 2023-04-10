package br.com.teste.sicredi.service;

import br.com.teste.sicredi.domain.Sessao;
import br.com.teste.sicredi.exception.DataLimiteException;
import br.com.teste.sicredi.exception.PautaNaoExisteException;
import br.com.teste.sicredi.exception.SessaoException;
import br.com.teste.sicredi.mapper.SessaoMapper;
import br.com.teste.sicredi.repository.SessaoRepository;
import br.com.teste.sicredi.representation.request.AbrirSessaoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
public class SessaoService {

    @Autowired
    private SessaoRepository repository;

    @Autowired
    private PautaService pautaService;

    @Autowired
    private SessaoMapper sessaoMapper;

    public void criarSessao(AbrirSessaoRequest request) {

        log.info("Verificando se já existe sessão para a pauta requisitada.");

        verificaSessaoExiste(request);

        log.info("Verificando se a data limite é válida.");

        LocalDateTime dataLimite = verificaDataLimite(request.getDataLimite());

        log.info("Verificando se a pauta existe.");

        verificaSeAPautaExiste(request);

        Sessao sessao = sessaoMapper.toDomain(request, dataLimite);

        log.info("Salvando sessão criada.");

        repository.save(sessao);
    }

    private void verificaSessaoExiste(AbrirSessaoRequest request) {

        if (repository.existsByIdPauta(request.getIdPauta())) {
            log.info("Já existe sessão para a pauta requisitada.");
            throw new SessaoException("A sessão dessa pauta já existe.");
        }
    }

    private LocalDateTime verificaDataLimite(LocalDateTime dataLimite) {

        log.info("Verificando se a data limite é nula (para criação de uma default) ou se ela é inválida.");

        return ofNullable(dataLimite)
                .map(this::validaDataLimite)
                .orElse(LocalDateTime.now().plusMinutes(1));
    }

    private LocalDateTime validaDataLimite(LocalDateTime request) {

        if (request.isBefore(LocalDateTime.now())) {
            log.info("Data limite é inválida.");
            throw new DataLimiteException("Data limite não pode ser antes da data atual.");
        }

        return request;
    }

    private void verificaSeAPautaExiste(AbrirSessaoRequest request) {

        if (!pautaService.existsById(request.getIdPauta())) {
            throw new PautaNaoExisteException("Pauta não existe.");
        }
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
