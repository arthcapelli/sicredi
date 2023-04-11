package br.com.teste.sicredi.mapper;

import br.com.teste.sicredi.representation.response.StatusResponse;
import org.springframework.stereotype.Component;

@Component
public class StatusMapper {

    public StatusResponse toResponse(String mensagem) {
        return StatusResponse.builder()
                .status(mensagem)
                .build();
    }
}
