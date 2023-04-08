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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private VotoMapper votoMapper;

    @Mock
    private AssociadoService associadoService;

    @Mock
    private PautaService pautaService;

    @Mock
    private SessaoService sessaoService;

    @InjectMocks
    private VotoService votoService;

    @Test
    public void testReceberVoto() {
        // Arrange
        VotoRequest request = new VotoRequest();
        request.setIdPauta(1);
        request.setIdAssociado(1);
        request.setValorVoto("SIM");

        Voto voto = Voto.builder()
                .id(1)
                .idAssociado(1)
                .idPauta(1)
                .valorVoto("Sim")
                .build();

        when(votoMapper.toDomain(request)).thenReturn(voto);

        // Act
        votoService.receberVoto(request);

        // Assert
        verify(votoMapper).toDomain(request);
        verify(votoRepository).save(voto);
    }

    @Test
    public void testReceberVoto_AssociadoJaVotou() {
        // Arrange
        VotoRequest request = new VotoRequest();
        request.setIdPauta(1);
        request.setIdAssociado(1);

        when(votoRepository.existsByIdAssociadoAndIdPauta(request.getIdAssociado(), request.getIdPauta())).thenReturn(true);

        // Act & Assert
        assertThrows(VotoInvalidoException.class, () -> votoService.receberVoto(request));

        // Assert
        verify(votoRepository).existsByIdAssociadoAndIdPauta(request.getIdAssociado(), request.getIdPauta());
    }

    @Test
    public void testReceberVoto_LimiteDeVotosAtingido() {
        // Arrange
        VotoRequest request = new VotoRequest();
        request.setIdPauta(1);
        request.setIdAssociado(1);

        when(votoRepository.countByIdPauta(request.getIdPauta())).thenReturn(1);

        Pauta pauta = Pauta.builder()
                .limiteVotos(1)
                .build();
        when(pautaService.getById(request.getIdPauta())).thenReturn(Optional.of(pauta));

        // Act & Assert
        assertThrows(VotoInvalidoException.class, () -> votoService.receberVoto(request));

        // Assert
        verify(votoRepository).countByIdPauta(request.getIdPauta());
        verify(pautaService).getById(request.getIdPauta());
    }

    @Test
    public void testReceberVoto_SessaoEncerrada() {
        // Arrange
        VotoRequest request = new VotoRequest();
        request.setIdPauta(1);
        request.setIdAssociado(1);

        when(votoRepository.countByIdPauta(request.getIdPauta())).thenReturn(1);

        Pauta pauta = Pauta.builder()
                .limiteVotos(10)
                .build();

        when(pautaService.getById(request.getIdPauta())).thenReturn(Optional.of(pauta));
        when(sessaoService.sessaoEncerrada(1)).thenThrow(DataLimiteException.class);

        // Act & Assert
        assertThrows(DataLimiteException.class, () -> votoService.receberVoto(request));

        // Assert
        verify(sessaoService).sessaoEncerrada(request.getIdPauta());
    }

    @Test
    public void contagemVotosVencedor_sessaoNaoExiste_throwSessaoException() {
        // Arrange
        int idPauta = 1;
        when(sessaoService.verificaExisteSessaoParaPauta(idPauta)).thenReturn(false);

        // Act & Assert
        assertThrows(SessaoException.class, () -> votoService.contagemVotosVencedor(idPauta));
    }

    @Test
    public void contagemVotosVencedor_sessaoEmAberto_throwVotoInvalidoException() {
        // Arrange
        int idPauta = 1;
        when(sessaoService.verificaExisteSessaoParaPauta(idPauta)).thenReturn(true);
        when(sessaoService.sessaoEncerrada(idPauta)).thenReturn(false);

        // Act & Assert
        assertThrows(VotoInvalidoException.class, () -> votoService.contagemVotosVencedor(idPauta));
    }

    @Test
    public void contagemVotosVencedor_sessaoFechada_verificaVencedor() {
        // Arrange
        int idPauta = 1;

        when(sessaoService.verificaExisteSessaoParaPauta(idPauta)).thenReturn(true);
        when(sessaoService.sessaoEncerrada(idPauta)).thenReturn(true);

        List<Voto> votos = Arrays.asList(
                Voto.builder().idPauta(idPauta)
                        .valorVoto("Sim")
                        .idAssociado(1)
                        .build(),
                Voto.builder().idPauta(idPauta)
                        .valorVoto("Não")
                        .idAssociado(2)
                        .build(),
                Voto.builder().idPauta(idPauta)
                        .valorVoto("Sim")
                        .idAssociado(3)
                        .build()
        );

        when(votoRepository.findAllByIdPauta(idPauta)).thenReturn(votos);

        Pauta pauta = new Pauta();
        
        when(pautaService.getById(idPauta)).thenReturn(Optional.of(pauta));
        when(votoMapper.toVencedorResponse(eq(pauta), anyString())).thenReturn(new VencedorResponse());

        // Act
        votoService.contagemVotosVencedor(idPauta);

        // Assert
        verify(votoRepository).findAllByIdPauta(idPauta);
        verify(pautaService).getById(idPauta);
        verify(votoMapper).toVencedorResponse(eq(pauta), anyString());
    }

}