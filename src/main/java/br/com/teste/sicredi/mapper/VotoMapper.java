package br.com.teste.sicredi.mapper;

import br.com.teste.sicredi.domain.ValorVoto;
import br.com.teste.sicredi.domain.Voto;
import br.com.teste.sicredi.representation.request.VotoRequest;
import org.springframework.stereotype.Component;

@Component
public class VotoMapper {

    public Voto toDomain(final VotoRequest request) {
        return Voto.builder()
                .IdAssociado(request.getIdAssociado())
                .valorVoto(ValorVoto.valueOf(request.getValorVoto()))
                .IdPauta(request.getIdPauta())
                .build();
    }
}
