package br.com.teste.sicredi.controller;

import br.com.teste.sicredi.representation.request.CadastrarAssociadoRequest;
import br.com.teste.sicredi.representation.response.StatusResponse;
import br.com.teste.sicredi.service.AssociadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("associado")
public class AssociadoController {

    @Autowired
    AssociadoService associadoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void cadastrarAssociado(@RequestBody final CadastrarAssociadoRequest request) {
        associadoService.cadastrarAssociado(request);
    }

    @GetMapping("{cpf}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StatusResponse> consultaCpf(@PathVariable String cpf) {
        return Optional.ofNullable(associadoService.consultaCpf(cpf))
                .map(cpfResponse -> ResponseEntity.ok().body(cpfResponse))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
