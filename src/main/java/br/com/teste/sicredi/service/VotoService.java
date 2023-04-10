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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
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

        log.info("Validando se sessão ainda está aberta.");

        validaSessaoAindaAberta(request.getIdPauta());

        log.info("Verificando se o associando já votou nessa sessão.");

        validaAssociadoJaVotou(request.getIdAssociado(), request.getIdPauta());

        if (limiteDeVotosAtingido(request.getIdPauta())) {
            log.info("Limite de votos já foi atingido.");
            throw new VotoInvalidoException("Limite de votos atingido.");
        }

        Voto voto = votoMapper.toDomain(request);

        log.info("Salvando voto.");

        repository.save(voto);
    }

    private void validaSessaoAindaAberta(Integer idPauta) {

        if (sessaoService.sessaoEncerrada(idPauta)) {
            log.info("Sessão teve o tempo expirado e está encerrada.");
            throw new DataLimiteException("Sessão de votos encerrada para essa pauta.");
        }
    }

    private void validaAssociadoJaVotou(Integer idAssociado, Integer idPauta) {

        if (repository.existsByIdAssociadoAndIdPauta(idAssociado, idPauta)) {
            log.info("Associado já votou nessa pauta.");
            throw new VotoInvalidoException("Associado já votou nessa pauta.");
        }
    }

    public VencedorResponse contagemVotosVencedor(Integer idPauta) {

        if (!sessaoService.verificaExisteSessaoParaPauta(idPauta)) {
            log.info("Sessão de votos para essa pauta não existe.");
            throw new SessaoException("Sessão de votos para essa pauta não existe.");
        }

        if (limiteDeVotosAtingido(idPauta) || sessaoService.sessaoEncerrada(idPauta)) {
            List<Voto> todosVotosDaPauta = repository.findAllByIdPauta(idPauta);
            Pauta pauta = pautaService.getPautaById(idPauta).orElseThrow();

            log.info("Contabilizando votos 'Sim'.");

            int contagemSim = contagemVotos(todosVotosDaPauta, "Sim");

            log.info("Contabilizando votos 'Não'.");

            int contagemNao = contagemVotos(todosVotosDaPauta, "Não");

            log.info("Verificando vencedor.");

            return verificaVencedor(pauta, contagemSim, contagemNao);
        }
        log.info("Sessão ainda em aberto, não atingiu limite de data e/ou votos.");
        throw new SessaoException("Sessão ainda em aberto, não atingiu limite de data e/ou votos.");
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

}
