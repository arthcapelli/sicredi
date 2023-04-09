package br.com.teste.sicredi.service;

import br.com.teste.sicredi.domain.Pauta;
import br.com.teste.sicredi.domain.Sessao;
import br.com.teste.sicredi.exception.DataLimiteException;
import br.com.teste.sicredi.exception.PautaNaoExisteException;
import br.com.teste.sicredi.exception.SessaoException;
import br.com.teste.sicredi.mapper.SessaoMapper;
import br.com.teste.sicredi.repository.SessaoRepository;
import br.com.teste.sicredi.representation.request.AbrirSessaoRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SessaoServiceTest {

    @InjectMocks
    private SessaoService sessaoService;

    @Mock
    private SessaoRepository sessaoRepository;

    @Mock
    private PautaService pautaService;

    @Mock
    private SessaoMapper sessaoMapper;

    @Test
    public void testCriarSessao() {
        AbrirSessaoRequest request = new AbrirSessaoRequest();
        request.setIdPauta(1);
        request.setDataLimite(LocalDateTime.of(2023, 12, 12, 12, 0, 0));

        Pauta pauta = Pauta.builder()
                .id(1)
                .build();

        Sessao sessao = Sessao.builder()
                .idPauta(1)
                .dataLimite(request.getDataLimite())
                .build();

        when(pautaService.getPautaById(1)).thenReturn(Optional.of(pauta));
        when(sessaoMapper.toDomain(request, request.getDataLimite())).thenReturn(sessao);

        sessaoService.criarSessao(request);

        verify(sessaoRepository).save(sessao);
    }

    @Test(expected = SessaoException.class)
    public void testCriarSessaoSessaoJaExiste() {
        AbrirSessaoRequest request = new AbrirSessaoRequest();
        request.setIdPauta(1);

        when(sessaoRepository.existsByIdPauta(1)).thenReturn(true);

        sessaoService.criarSessao(request);
    }

    @Test(expected = PautaNaoExisteException.class)
    public void testCriarSessaoPautaNaoExiste() {
        AbrirSessaoRequest request = new AbrirSessaoRequest();
        request.setIdPauta(1);

        when(pautaService.getPautaById(1)).thenReturn(Optional.empty());

        sessaoService.criarSessao(request);
    }

    @Test(expected = DataLimiteException.class)
    public void testCriarSessaoDataInvalida() {
        AbrirSessaoRequest request = new AbrirSessaoRequest();
        request.setIdPauta(1);
        request.setDataLimite(LocalDateTime.of(2022, 12, 12, 12, 0, 0));

        sessaoService.criarSessao(request);
    }

    @Test
    public void testSessaoEncerrada() {
        Sessao sessao = Sessao.builder()
                .dataLimite(LocalDateTime.of(2022, 12, 12, 12, 0, 0))
                .build();

        when(sessaoRepository.findByIdPauta(1)).thenReturn(sessao);

        boolean encerrada = sessaoService.sessaoEncerrada(1);

        assertTrue(encerrada);
    }

    @Test
    public void testSessaoNaoEncerrada() {
        Sessao sessao = Sessao.builder()
                .dataLimite(LocalDateTime.of(2023, 12, 12, 12, 0, 0))
                .build();

        when(sessaoRepository.findByIdPauta(1)).thenReturn(sessao);

        boolean encerrada = sessaoService.sessaoEncerrada(1);

        assertFalse(encerrada);
    }

    @Test
    public void testVerificaExisteSessaoParaPauta() {

        when(sessaoRepository.existsByIdPauta(1)).thenReturn(true);

        boolean existeSessao = sessaoService.verificaExisteSessaoParaPauta(1);

        assertTrue(existeSessao);
    }
}