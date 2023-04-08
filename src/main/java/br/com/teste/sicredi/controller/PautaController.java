package br.com.teste.sicredi.controller;

import br.com.teste.sicredi.representation.request.CadastrarPautaRequest;
import br.com.teste.sicredi.service.PautaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("pauta")
public class PautaController {

    @Autowired
    PautaService pautaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void cadastrarPauta(@RequestBody final CadastrarPautaRequest request) {
        pautaService.cadastrarPauta(request);
    }
}
