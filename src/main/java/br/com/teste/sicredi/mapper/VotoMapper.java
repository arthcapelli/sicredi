package br.com.teste.sicredi.mapper;

import br.com.teste.sicredi.domain.Associado;
import br.com.teste.sicredi.domain.Pauta;
import br.com.teste.sicredi.domain.ValorVoto;
import br.com.teste.sicredi.domain.Voto;
import br.com.teste.sicredi.representation.request.VotoRequest;
import org.springframework.stereotype.Component;

@Component
public class VotoMapper {

    public Voto toDomain(final VotoRequest request, final Associado associado, final Pauta pauta) {
        return Voto.builder()
                .associado(associado)
                .valorVoto(ValorVoto.valueOf(request.getValorVoto()))
                .pauta(pauta)
                .build();
    }
}
