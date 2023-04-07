package br.com.teste.sicredi.representation.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VotoRequest {

    private String cpfAssociado;

    private String valorVoto;

    private String idPauta;
}
