package br.com.teste.sicredi.service;

import br.com.teste.sicredi.domain.Pauta;
import br.com.teste.sicredi.exception.DataCriacaoException;
import br.com.teste.sicredi.mapper.PautaMapper;
import br.com.teste.sicredi.repository.PautaRepository;
import br.com.teste.sicredi.representation.request.CadastrarPautaRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PautaServiceTest {

    @InjectMocks
    private PautaService pautaService;

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private PautaMapper pautaMapper;

    @Test
    public void testCadastrarPauta() {
        CadastrarPautaRequest request = CadastrarPautaRequest.builder()
                .titulo("Teste de pauta")
                .limiteVotos(10)
                .dataCriacao(LocalDateTime.of(2023, 12, 12, 12, 0, 0))
                .build();

        Pauta pauta = Pauta.builder()
                .titulo("Teste")
                .limiteVotos(10)
                .dataCriacao(LocalDateTime.of(2023, 12, 12, 12, 0, 0))
                .build();

        when(pautaMapper.toDomain(request)).thenReturn(pauta);

        pautaService.cadastrarPauta(request);

        verify(pautaRepository, times(1)).save(pauta);
    }

    @Test(expected = DataCriacaoException.class)
    public void testCadastrarPautaComDataCriacaoInvalida() {
        CadastrarPautaRequest request = CadastrarPautaRequest.builder()
                .titulo("Teste de pauta")
                .limiteVotos(10)
                .dataCriacao(LocalDateTime.of(2022, 12, 12, 12, 0, 0))
                .build();

        pautaService.cadastrarPauta(request);
    }
}