package br.com.teste.sicredi.mapper;

import br.com.teste.sicredi.domain.Pauta;
import br.com.teste.sicredi.domain.Voto;
import br.com.teste.sicredi.representation.request.VotoRequest;
import br.com.teste.sicredi.representation.response.VencedorResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class VotoMapperTest {

    private VotoMapper votoMapper = new VotoMapper();

    @Test
    public void testToDomain() {
        VotoRequest request = new VotoRequest();
        request.setIdAssociado(1);
        request.setValorVoto("SIM");
        request.setIdPauta(1);

        Voto voto = votoMapper.toDomain(request);

        assertEquals(1, voto.getIdAssociado());
        assertEquals("Sim", voto.getValorVoto());
        assertEquals(1, voto.getIdPauta());
    }

    @Test
    public void testToDomainComVotoValorNao() {
        VotoRequest request = new VotoRequest();
        request.setIdAssociado(1);
        request.setValorVoto("NAO");
        request.setIdPauta(1);

        Voto voto = votoMapper.toDomain(request);

        assertEquals(1, voto.getIdAssociado());
        assertEquals("Não", voto.getValorVoto());
        assertEquals(1, voto.getIdPauta());
    }

    @Test
    public void testToVencedorResponse() {
        Pauta pauta = Pauta.builder()
                .titulo("Teste de Pauta")
                .build();
        String resultado = "Aprovada";

        VencedorResponse response = votoMapper.toVencedorResponse(pauta, resultado);

        assertEquals("A pauta Teste de Pauta teve o resultado: Aprovada", response.getFraseVencedor());
    }
}