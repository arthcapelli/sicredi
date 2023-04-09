package br.com.teste.sicredi.service;

import br.com.teste.sicredi.domain.Pauta;
import br.com.teste.sicredi.domain.Voto;
import br.com.teste.sicredi.exception.DataLimiteException;
import br.com.teste.sicredi.exception.SessaoException;
import br.com.teste.sicredi.exception.VotoInvalidoException;
import br.com.teste.sicredi.mapper.VotoMapper;
import br.com.teste.sicredi.repository.VotoRepository;
import br.com.teste.sicredi.representation.request.VotoRequest;
import br.com.teste.sicredi.representation.response.VencedorResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VotoServiceTest {

    @InjectMocks
    private VotoService votoService;

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private VotoMapper votoMapper;

    @Mock
    private PautaService pautaService;

    @Mock
    private SessaoService sessaoService;

    @Test
    public void testReceberVoto() {
        VotoRequest request = new VotoRequest();
        request.setIdPauta(1);
        request.setIdAssociado(1);
        request.setValorVoto("SIM");

        Voto voto = buildVoto("Sim", 1, 1, 1);

        when(votoMapper.toDomain(request)).thenReturn(voto);

        votoService.receberVoto(request);

        verify(votoMapper).toDomain(request);
        verify(votoRepository).save(voto);
    }

    @Test(expected = VotoInvalidoException.class)
    public void testReceberVotoAssociadoJaVotou() {
        VotoRequest request = new VotoRequest();
        request.setIdPauta(1);
        request.setIdAssociado(1);

        when(votoRepository.existsByIdAssociadoAndIdPauta(request.getIdAssociado(), request.getIdPauta())).thenReturn(true);

        votoService.receberVoto(request);

        verify(votoRepository).existsByIdAssociadoAndIdPauta(request.getIdAssociado(), request.getIdPauta());
    }

    @Test(expected = VotoInvalidoException.class)
    public void testReceberVotoLimiteDeVotosAtingido() {
        VotoRequest request = new VotoRequest();
        request.setIdPauta(1);
        request.setIdAssociado(1);

        Pauta pauta = Pauta.builder()
                .limiteVotos(1)
                .build();

        when(votoRepository.countByIdPauta(request.getIdPauta())).thenReturn(1);
        when(pautaService.getById(request.getIdPauta())).thenReturn(Optional.of(pauta));

        votoService.receberVoto(request);

        verify(votoRepository).countByIdPauta(request.getIdPauta());
        verify(pautaService).getById(request.getIdPauta());
    }

    @Test(expected = DataLimiteException.class)
    public void testReceberVotoSessaoEncerrada() {
        VotoRequest request = new VotoRequest();
        request.setIdPauta(1);
        request.setIdAssociado(1);

        when(sessaoService.sessaoEncerrada(1)).thenReturn(true);

        votoService.receberVoto(request);

        verify(sessaoService).sessaoEncerrada(request.getIdPauta());
    }

    @Test(expected = SessaoException.class)
    public void testContagemVotosVencedorSessaoNaoExisteSessaoException() {
        int idPauta = 1;
        when(sessaoService.verificaExisteSessaoParaPauta(idPauta)).thenReturn(false);

        votoService.contagemVotosVencedor(idPauta);
    }

    @Test(expected = VotoInvalidoException.class)
    public void testContagemVotosVencedorSessaoEmAbertoVotoInvalidoException() {
        int idPauta = 1;
        when(sessaoService.verificaExisteSessaoParaPauta(idPauta)).thenReturn(true);
        when(sessaoService.sessaoEncerrada(idPauta)).thenReturn(false);

         votoService.contagemVotosVencedor(idPauta);
    }

    @Test
    public void testContagemVotosVencedorSessaoFechadaVerificaVencedorSim() {
        Integer idPauta = 1;

        Pauta pauta = Pauta.builder()
                .id(idPauta)
                .limiteVotos(3)
                .titulo("Teste")
                .build();

        List<Voto> votos = Arrays.asList(buildVoto("Sim", 1, 1, 1),
                buildVoto("Sim", 1, 1, 1),
                buildVoto("Sim", 1, 1, 1));

        when(sessaoService.verificaExisteSessaoParaPauta(idPauta)).thenReturn(true);
        when(sessaoService.sessaoEncerrada(idPauta)).thenReturn(true);
        when(votoRepository.findAllByIdPauta(idPauta)).thenReturn(votos);
        when(pautaService.getById(idPauta)).thenReturn(Optional.of(pauta));
        when(votoMapper.toVencedorResponse(eq(pauta), anyString())).thenReturn(new VencedorResponse());

        VencedorResponse result = votoService.contagemVotosVencedor(idPauta);

        verify(votoRepository).findAllByIdPauta(idPauta);
        verify(pautaService, times(2)).getById(idPauta);
        verify(votoMapper).toVencedorResponse(eq(pauta), anyString());
        assertNotNull(result);
    }

    @Test
    public void testContagemVotosVencedorSessaoFechadaVerificaVencedorNao() {
        Integer idPauta = 1;

        Pauta pauta = Pauta.builder()
                .id(idPauta)
                .limiteVotos(3)
                .titulo("Teste")
                .build();

        List<Voto> votos = Arrays.asList(buildVoto("Não", 1, 1, 1),
                buildVoto("Não", 1, 1, 1),
                buildVoto("Não", 1, 1, 1));

        when(sessaoService.verificaExisteSessaoParaPauta(idPauta)).thenReturn(true);
        when(sessaoService.sessaoEncerrada(idPauta)).thenReturn(true);
        when(votoRepository.findAllByIdPauta(idPauta)).thenReturn(votos);
        when(pautaService.getById(idPauta)).thenReturn(Optional.of(pauta));
        when(votoMapper.toVencedorResponse(eq(pauta), anyString())).thenReturn(new VencedorResponse());

        VencedorResponse result = votoService.contagemVotosVencedor(idPauta);

        verify(votoRepository).findAllByIdPauta(idPauta);
        verify(pautaService, times(2)).getById(idPauta);
        verify(votoMapper).toVencedorResponse(eq(pauta), anyString());
        assertNotNull(result);
    }

    @Test
    public void testContagemVotosVencedorSessaoFechadaVerificaVencedorEmpate() {
        Integer idPauta = 1;

        List<Voto> votos = Arrays.asList(buildVoto("Não", 1, 1, 1),
                buildVoto("Não", 1, 1, 1),
                buildVoto("Sim", 1, 1, 1),
                buildVoto("Sim", 1, 1, 1));

        Pauta pauta = Pauta.builder()
                .id(idPauta)
                .limiteVotos(4)
                .titulo("Teste")
                .build();

        when(sessaoService.verificaExisteSessaoParaPauta(idPauta)).thenReturn(true);
        when(sessaoService.sessaoEncerrada(idPauta)).thenReturn(true);
        when(votoRepository.findAllByIdPauta(idPauta)).thenReturn(votos);
        when(pautaService.getById(idPauta)).thenReturn(Optional.of(pauta));
        when(votoMapper.toVencedorResponse(eq(pauta), anyString())).thenReturn(new VencedorResponse());

        VencedorResponse result = votoService.contagemVotosVencedor(idPauta);

        verify(votoRepository).findAllByIdPauta(idPauta);
        verify(pautaService, times(2)).getById(idPauta);
        verify(votoMapper).toVencedorResponse(eq(pauta), anyString());
        assertNotNull(result);
    }

    private Voto buildVoto(String valorVoto, Integer id, Integer idAssociado, Integer idPauta) {
        return Voto.builder()
                .id(id)
                .idAssociado(idAssociado)
                .idPauta(idPauta)
                .valorVoto(valorVoto)
                .build();
    }

}