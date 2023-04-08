package br.com.teste.sicredi.service;

import br.com.teste.sicredi.domain.Pauta;
import br.com.teste.sicredi.domain.ValorVoto;
import br.com.teste.sicredi.domain.Voto;
import br.com.teste.sicredi.exception.DataLimiteException;
import br.com.teste.sicredi.exception.VotoInvalidoException;
import br.com.teste.sicredi.mapper.VotoMapper;
import br.com.teste.sicredi.repository.VotoRepository;
import br.com.teste.sicredi.representation.request.VotoRequest;
import br.com.teste.sicredi.representation.response.VencedorResponse;
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
        validaSessaoAindaAberta(request.getIdPauta());
        validaLimiteDeVotosAtingido(request.getIdPauta());
        validaAssociadoJaVotou(request.getIdAssociado(), request.getIdPauta());

        Voto voto = votoMapper.toDomain(request);
        repository.save(voto);
    }

    public VencedorResponse contagemVotosVencedor(Integer idPauta) {
        List<Voto> todosVotosDaPauta = repository.findAllByIdPauta(idPauta);
        Optional<Pauta> pauta = pautaService.getById(idPauta);
        int contagemSim = contagemVotos(todosVotosDaPauta, "Sim");
        int contagemNao = contagemVotos(todosVotosDaPauta, "Não");

        return verificaVencedor(pauta, contagemSim, contagemNao);
    }

    private int contagemVotos(List<Voto> votosTotais, String valorVoto) {
        return votosTotais
                .stream()
                .filter(voto -> voto.getValorVoto().equals(valorVoto))
                .toList()
                .size();
    }

    private VencedorResponse verificaVencedor(Optional<Pauta> pauta, int contagemSim, int contagemNao) {
        if (contagemSim > contagemNao) {
            return votoMapper.toVencedorResponse(pauta, ValorVoto.SIM.getDescription());
        } else if (contagemNao > contagemSim) {
            return votoMapper.toVencedorResponse(pauta, ValorVoto.NAO.getDescription());
        }
        return votoMapper.toVencedorResponse(pauta, "Empate");
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

    private void validaSessaoAindaAberta(Integer idPauta) {
        if (sessaoService.sessaoEncerrada(idPauta)) {
            throw new DataLimiteException("Sessão de votos encerrada para essa pauta.");
        }
    }

}
