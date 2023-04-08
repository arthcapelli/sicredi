package br.com.teste.sicredi.mapper;

import br.com.teste.sicredi.domain.Sessao;
import br.com.teste.sicredi.representation.request.AbrirSessaoRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SessaoMapper {

    public Sessao toDomain(AbrirSessaoRequest request, LocalDateTime dataLimite) {
        return Sessao.builder()
                .idPauta(request.getIdPauta())
                .dataLimite(dataLimite)
                .build();
    }


}
