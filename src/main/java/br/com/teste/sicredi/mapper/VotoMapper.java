package br.com.teste.sicredi.mapper;

import br.com.teste.sicredi.domain.Pauta;
import br.com.teste.sicredi.domain.ValorVoto;
import br.com.teste.sicredi.domain.Voto;
import br.com.teste.sicredi.representation.request.VotoRequest;
import br.com.teste.sicredi.representation.response.VencedorResponse;
import org.springframework.stereotype.Component;

@Component
public class VotoMapper {

    public Voto toDomain(final VotoRequest request) {
        return Voto.builder()
                .idAssociado(request.getIdAssociado())
                .valorVoto(ValorVoto.valueOf(request.getValorVoto()).getDescription())
                .idPauta(request.getIdPauta())
                .build();
    }

    public VencedorResponse toVencedorResponse(final Pauta pauta, String resultado) {
        return VencedorResponse.builder()
                .fraseVencedor(montaFraseVencedor(pauta.getTitulo(), resultado))
                .build();
    }

    private String montaFraseVencedor(String titulo, String resultado) {
        return "A pauta " + titulo + " teve o resultado: " + resultado;
    }
}
