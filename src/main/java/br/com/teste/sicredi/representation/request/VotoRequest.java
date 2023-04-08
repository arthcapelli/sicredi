package br.com.teste.sicredi.representation.request;

import lombok.*;

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
