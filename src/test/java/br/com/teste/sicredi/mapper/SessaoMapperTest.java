package br.com.teste.sicredi.mapper;

import br.com.teste.sicredi.domain.Sessao;
import br.com.teste.sicredi.representation.request.AbrirSessaoRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class SessaoMapperTest {
    private SessaoMapper sessaoMapper = new SessaoMapper();

    @Test
    public void testToDomain() {
        AbrirSessaoRequest request = new AbrirSessaoRequest();
        request.setIdPauta(1);
        LocalDateTime dataLimite = LocalDateTime.of(2023, 12,12,12, 0, 0);

        Sessao sessao = sessaoMapper.toDomain(request, dataLimite);

        assertEquals(1, sessao.getIdPauta());
        assertEquals(dataLimite, sessao.getDataLimite());
    }
}