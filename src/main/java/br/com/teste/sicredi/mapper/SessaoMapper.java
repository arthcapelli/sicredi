package br.com.teste.sicredi.mapper;

import br.com.teste.sicredi.domain.Sessao;
import br.com.teste.sicredi.exception.DataLimiteException;
import br.com.teste.sicredi.representation.request.AbrirSessaoRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static java.util.Optional.ofNullable;

@Component
public class SessaoMapper {

    public Sessao toDomain(AbrirSessaoRequest request) {
        LocalDateTime dataLimite = ofNullable(request.getDataLimite())
                .map(this::validaDataLimite)
                .orElse(LocalDateTime.now().plusMinutes(1));

        return Sessao.builder()
                .idPauta(request.getIdPauta())
                .dataLimite(dataLimite)
                .build();
    }

    private LocalDateTime validaDataLimite(LocalDateTime request) {
        if (request.isBefore(LocalDateTime.now())) {
            throw new DataLimiteException("Data limite não pode ser antes da data atual.");
        }

        return request;
    }
}
