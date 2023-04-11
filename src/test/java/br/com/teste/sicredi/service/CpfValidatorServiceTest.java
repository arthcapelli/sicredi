package br.com.teste.sicredi.service;

import br.com.teste.sicredi.web.CpfValidatorRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CpfValidatorServiceTest {

    @InjectMocks
    private CpfValidatorService cpfValidatorService;

    @Mock
    private CpfValidatorRepository cpfValidatorRepository;

    @Test
    public void testVerificaCpfValido() {
        String cpf = "12345678901";
        when(cpfValidatorRepository.validaCpf(cpf)).thenReturn(true);

        boolean result = cpfValidatorService.verificaCpfValido(cpf);

        assertTrue(result);
    }

    @Test
    public void testVerificaCpfInvalido() {
        String cpf = "12345678901";
        when(cpfValidatorRepository.validaCpf(cpf)).thenReturn(false);

        boolean result = cpfValidatorService.verificaCpfValido(cpf);

        assertFalse(result);
    }

}