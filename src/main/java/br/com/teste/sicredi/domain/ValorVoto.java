package br.com.teste.sicredi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ValorVoto {

    SIM("Sim"),
    NAO("Não");

    @Getter
    private final String description;
}
