package br.com.teste.sicredi.mapper;

import br.com.teste.sicredi.domain.Pauta;
import br.com.teste.sicredi.representation.request.CadastrarPautaRequest;
import org.springframework.stereotype.Component;

@Component
public class PautaMapper {

    public Pauta toDomain(final CadastrarPautaRequest request) {
        return Pauta.builder()
                .titulo(request.getTitulo())
                .limiteVotos(request.getLimiteVotos())
                .dataCriacao(request.getDataCriacao())
                .build();
    }
}
