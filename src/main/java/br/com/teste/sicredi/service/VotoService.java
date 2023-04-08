package br.com.teste.sicredi.service;

import br.com.teste.sicredi.domain.Pauta;
import br.com.teste.sicredi.domain.ValorVoto;
import br.com.teste.sicredi.domain.Voto;
import br.com.teste.sicredi.exception.DataLimiteException;
import br.com.teste.sicredi.exception.VotoInvalidoException;
import br.com.teste.sicredi.mapper.VotoMapper;
import br.com.teste.sicredi.repository.VotoRepository;
import br.com.teste.sicredi.representation.request.VotoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class VotoService {

    @Autowired
    private VotoRepository repository;

    @Autowired
    private VotoMapper votoMapper;

    @Autowired
    private AssociadoService associadoService;

    @Autowired
    private PautaService pautaService;

    @Autowired
    private SessaoService sessaoService;

    public void receberVoto(VotoRequest request) {
        validaSessao(request.getIdPauta());
        validaLimiteDeVotosAtingido(request.getIdPauta());
        validaAssociadoJaVotou(request.getIdAssociado(), request.getIdPauta());

        Voto voto = votoMapper.toDomain(request);
        repository.save(voto);
    }

    public String contagemVotos(Integer idPauta) {
        List<Voto> contagemVotos = repository.findAllByIdPauta(idPauta);
        Integer contagemSim = contagemVotos(contagemVotos, "Sim");
        Integer contagemNao = contagemVotos(contagemVotos, "Não");

        if (contagemSim > contagemNao) {
            return ValorVoto.SIM.getDescription();
        }

        return ValorVoto.NAO.getDescription();
    }

    private int contagemVotos(List<Voto> contagemVotos, String valorVoto) {
        return contagemVotos
                .stream()
                .filter(voto -> voto.getValorVoto().equals(valorVoto))
                .toList()
                .size();
    }

    private void validaLimiteDeVotosAtingido(Integer idPauta) {
        Optional<Pauta> pautaASerVotada = pautaService.getById(idPauta);

        pautaASerVotada.ifPresent(pauta -> {
            if (Objects.equals(pauta.getLimiteVotos(), repository.countByIdPauta(idPauta))) {
                throw new VotoInvalidoException("Limite de votos atingido.");
            }
        });
    }

    private void validaAssociadoJaVotou(Integer idAssociado, Integer idPauta) {
        if (repository.existsByIdAssociadoAndIdPauta(idAssociado, idPauta)) {
            throw new VotoInvalidoException("Associado já votou nessa pauta.");
        }
    }

    private void validaSessao(Integer idPauta) {
        if (sessaoService.sessaoEncerrada(idPauta)) {
            throw new DataLimiteException("Sessão de votos encerrada para essa pauta.");
        }
    }

}
