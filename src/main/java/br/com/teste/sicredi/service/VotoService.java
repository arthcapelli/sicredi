package br.com.teste.sicredi.service;

import br.com.teste.sicredi.domain.Pauta;
import br.com.teste.sicredi.domain.ValorVoto;
import br.com.teste.sicredi.domain.Voto;
import br.com.teste.sicredi.exception.DataLimiteException;
import br.com.teste.sicredi.exception.SessaoException;
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

        validaAssociadoJaVotou(request.getIdAssociado(), request.getIdPauta());

        if (limiteDeVotosAtingido(request.getIdPauta())) {
            throw new VotoInvalidoException("Limite de votos atingido.");
        }

        Voto voto = votoMapper.toDomain(request);
        repository.save(voto);
    }

    public VencedorResponse contagemVotosVencedor(Integer idPauta) {

        if (!sessaoService.verificaExisteSessaoParaPauta(idPauta)) {
            throw new SessaoException("Sessão de votos para essa pauta não existe.");
        }

        if (limiteDeVotosAtingido(idPauta) || sessaoService.sessaoEncerrada(idPauta)) {
            List<Voto> todosVotosDaPauta = repository.findAllByIdPauta(idPauta);
            Pauta pauta = pautaService.getPautaById(idPauta).orElseThrow();
            int contagemSim = contagemVotos(todosVotosDaPauta, "Sim");
            int contagemNao = contagemVotos(todosVotosDaPauta, "Não");

            return verificaVencedor(pauta, contagemSim, contagemNao);
        }

        throw new VotoInvalidoException("Sessão ainda em aberto, não atingiu limite de data e/ou votos.");
    }

    private int contagemVotos(List<Voto> votosTotais, String valorVoto) {
        return votosTotais
                .stream()
                .filter(voto -> voto.getValorVoto().equals(valorVoto))
                .toList()
                .size();
    }

    private VencedorResponse verificaVencedor(Pauta pauta, int contagemSim, int contagemNao) {

        if (contagemSim > contagemNao) {
            return votoMapper.toVencedorResponse(pauta, ValorVoto.SIM.getDescription());
        } else if (contagemNao > contagemSim) {
            return votoMapper.toVencedorResponse(pauta, ValorVoto.NAO.getDescription());
        }

        return votoMapper.toVencedorResponse(pauta, "Empate");
    }

    private boolean limiteDeVotosAtingido(Integer idPauta) {

        Optional<Pauta> pautaASerVotada = pautaService.getPautaById(idPauta);

        return pautaASerVotada.map(pauta ->
                        Objects.equals(pauta.getLimiteVotos(), repository.countByIdPauta(idPauta)))
                .orElse(false);
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
