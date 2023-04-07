package br.com.teste.sicredi.representation.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CadastrarPautaRequest {

    private String titulo;

    private Integer limiteVotos;

    private LocalDateTime dataCriacao;
}
