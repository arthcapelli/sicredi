package br.com.teste.sicredi.mapper;

import br.com.teste.sicredi.domain.Associado;
import br.com.teste.sicredi.representation.request.CadastrarAssociadoRequest;
import org.springframework.stereotype.Component;

@Component
public class AssociadoMapper {

    public Associado toDomain(CadastrarAssociadoRequest request) {
        return Associado.builder()
                .cpf(request.getCpf())
                .build();
    }
}
