package br.com.teste.sicredi.service;

import br.com.teste.sicredi.domain.Sessao;
import br.com.teste.sicredi.exception.PautaNaoExisteException;
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
        Sessao sessao = pautaService.getById(request.getIdPauta())
                .map(pauta -> sessaoMapper.toDomain(request))
                .orElseThrow(() -> new PautaNaoExisteException("Pauta não existe."));

        repository.save(sessao);
    }

    public boolean sessaoEncerrada(Integer idPauta) {
        return ofNullable(repository.findByIdPauta(idPauta))
                .map(sessao -> sessao.getDataLimite().isBefore(LocalDateTime.now()))
                .orElse(false);
    }

}
