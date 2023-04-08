package br.com.teste.sicredi.controller;

import br.com.teste.sicredi.representation.request.CadastrarAssociadoRequest;
import br.com.teste.sicredi.service.AssociadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}
