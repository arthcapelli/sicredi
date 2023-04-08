package br.com.teste.sicredi.controller;

import br.com.teste.sicredi.representation.request.AbrirSessaoRequest;
import br.com.teste.sicredi.service.SessaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sessao")
public class SessaoController {

    @Autowired
    SessaoService sessaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void abrirSessao(@RequestBody final AbrirSessaoRequest request) {
        sessaoService.criarSessao(request);
    }
}
