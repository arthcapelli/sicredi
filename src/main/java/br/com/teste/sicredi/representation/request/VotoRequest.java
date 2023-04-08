package br.com.teste.sicredi.representation.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VotoRequest {

    private Integer idAssociado;

    private String valorVoto;

    private Integer idPauta;
}
