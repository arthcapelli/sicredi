package br.com.teste.sicredi.mapper;

import br.com.teste.sicredi.domain.Associado;
import br.com.teste.sicredi.representation.request.CadastrarAssociadoRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class AssociadoMapperTest {

    private final AssociadoMapper associadoMapper = new AssociadoMapper();

    @Test
    public void testToDomain() {
        CadastrarAssociadoRequest request = CadastrarAssociadoRequest.builder().cpf("12345678900").build();

        Associado associado = associadoMapper.toDomain(request);

        assertEquals("12345678900", associado.getCpf());
    }

}