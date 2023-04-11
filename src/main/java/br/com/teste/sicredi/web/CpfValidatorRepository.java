package br.com.teste.sicredi.web;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "cpfValidator", url = "https://valida-cpf-sicredi-production.up.railway.app")
public interface CpfValidatorRepository {

    @GetMapping(value = "/cpf/{cpf}")
    boolean validaCpf(@PathVariable String cpf);
}
