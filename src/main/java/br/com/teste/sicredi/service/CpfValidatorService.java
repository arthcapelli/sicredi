package br.com.teste.sicredi.service;

import br.com.teste.sicredi.web.CpfValidatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CpfValidatorService {

    @Autowired
    private CpfValidatorRepository cpfValidatorRepository;

    public boolean verificaCpfValido(String cpf) {

        return cpfValidatorRepository.validaCpf(cpf);
    }
}
