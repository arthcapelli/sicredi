package br.com.teste.sicredi.mapper;

import br.com.teste.sicredi.domain.Pauta;
import br.com.teste.sicredi.representation.request.CadastrarPautaRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class PautaMapperTest {

    private final PautaMapper pautaMapper = new PautaMapper();

    @Test
    public void testToDomain() {
        CadastrarPautaRequest request = CadastrarPautaRequest.builder()
                .titulo("Teste de pauta")
                .limiteVotos(10)
                .dataCriacao(LocalDateTime.of(2022, 12, 12, 12, 0, 0))
                .build();


        Pauta pauta = pautaMapper.toDomain(request);

        assertEquals("Teste de pauta", pauta.getTitulo());
        assertEquals(10, pauta.getLimiteVotos());
        assertEquals(request.getDataCriacao(), pauta.getDataCriacao());
    }

}



