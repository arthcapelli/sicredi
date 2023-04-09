package br.com.teste.sicredi.service;

import br.com.teste.sicredi.domain.Associado;
import br.com.teste.sicredi.mapper.AssociadoMapper;
import br.com.teste.sicredi.repository.AssociadoRepository;
import br.com.teste.sicredi.representation.request.CadastrarAssociadoRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AssociadoServiceTest {

    @InjectMocks
    private AssociadoService service;

    @Mock
    private AssociadoRepository repository;

    @Mock
    private AssociadoMapper mapper;

    @Test
    public void testCadastrarAssociado() {
        CadastrarAssociadoRequest request = CadastrarAssociadoRequest.builder()
                .cpf("12345678900")
                .build();

        Associado associado = Associado.builder()
                .cpf("12345678900")
                .build();

        when(mapper.toDomain(request)).thenReturn(associado);

        service.cadastrarAssociado(request);

        verify(mapper, times(1)).toDomain(request);
        verify(repository, times(1)).save(associado);
    }
}