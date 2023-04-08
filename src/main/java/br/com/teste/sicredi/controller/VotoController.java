package br.com.teste.sicredi.controller;

import br.com.teste.sicredi.representation.request.VotoRequest;
import br.com.teste.sicredi.representation.response.VencedorResponse;
import br.com.teste.sicredi.service.VotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("voto")
public class VotoController {

    @Autowired
    VotoService votoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void votarPauta(@RequestBody final VotoRequest request) {
        votoService.receberVoto(request);
    }

    @GetMapping("{idPauta}")
    @ResponseStatus(HttpStatus.OK)
    public VencedorResponse resultadoPauta(@PathVariable Integer idPauta) {
        return votoService.contagemVotosVencedor(idPauta);
    }
}
