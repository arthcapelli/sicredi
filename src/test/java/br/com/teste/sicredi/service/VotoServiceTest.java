package br.com.teste.sicredi.service;

import br.com.teste.sicredi.domain.Pauta;
import br.com.teste.sicredi.domain.Voto;
import br.com.teste.sicredi.exception.DataLimiteException;
import br.com.teste.sicredi.exception.VotoInvalidoException;
import br.com.teste.sicredi.mapper.VotoMapper;
import br.com.teste.sicredi.repository.VotoRepository;
import br.com.teste.sicredi.representation.request.VotoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VotoServiceTest {

    @InjectMocks
    private VotoService votoService;

    @Mock
    private VotoRepository repository;

    @Mock
    private VotoMapper votoMapper;

    @Mock
    private AssociadoService associadoService;

    @Mock
    private PautaService pautaService;

    @Mock
    private SessaoService sessaoService;

    private VotoRequest votoRequest;

    private LocalDateTime todayAt6;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        votoRequest = new VotoRequest();
        votoRequest.setIdPauta(1);
        votoRequest.setIdAssociado(1);
        votoRequest.setValorVoto("Sim");

        todayAt6 = LocalDate.now().atTime(6, 0);
    }

    @Test
    public void testReceberVotoValid() {
        when(sessaoService.sessaoEncerrada(1)).thenReturn(false);
        when(pautaService.getById(1)).thenReturn(Optional.of(new Pauta(1, "titulo", 10, LocalDateTime.now().plusDays(1))));
        when(repository.countByIdPauta(1)).thenReturn(5);
        when(repository.existsByIdAssociadoAndIdPauta(1, 1)).thenReturn(false);

        votoService.receberVoto(votoRequest);

        // Verify that the save method was called once
        verify(repository, times(1)).save(any(Voto.class));
    }

    @Test
    public void testReceberVotoInvalidLimiteVotos() {
        when(pautaService.getById(1)).thenReturn(Optional.of(new Pauta(1, "titulo", 10, todayAt6)));
        when(repository.countByIdPauta(1)).thenReturn(10);

        // Verify that the VotoInvalidoException is thrown with the correct message
        VotoInvalidoException exception = assertThrows(VotoInvalidoException.class, () -> {
            votoService.receberVoto(votoRequest);
        });
        assertEquals("Limite de votos atingido.", exception.getMessage());

        // Verify that the save method was not called
        verify(repository, never()).save(any(Voto.class));
    }

    @Test
    public void testReceberVotoInvalidAssociadoJaVotou() {
        when(pautaService.getById(1)).thenReturn(Optional.of(new Pauta(1, "titulo", 10, todayAt6)));
        when(repository.countByIdPauta(1)).thenReturn(5);
        when(repository.existsByIdAssociadoAndIdPauta(1, 1)).thenReturn(true);

        // Verify that the VotoInvalidoException is thrown with the correct message
        VotoInvalidoException exception = assertThrows(VotoInvalidoException.class, () -> {
            votoService.receberVoto(votoRequest);
        });
        assertEquals("Associado já votou nessa pauta.", exception.getMessage());

        // Verify that the save method was not called
        verify(repository, never()).save(any(Voto.class));
    }

    @Test
    public void testReceberVotoInvalidSessaoEncerrada() {
        when(sessaoService.sessaoEncerrada(1)).thenReturn(true);

        DataLimiteException exception = assertThrows(DataLimiteException.class, () -> {
            votoService.receberVoto(votoRequest);
        });

        assertEquals("Sessão de votos encerrada para essa pauta.", exception.getMessage());

        // Verify that the save method was not called
        verify(repository, never()).save(any(Voto.class));
    }
}