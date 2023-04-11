package br.com.teste.sicredi.service;

import br.com.teste.sicredi.domain.Associado;
import br.com.teste.sicredi.domain.Status;
import br.com.teste.sicredi.mapper.AssociadoMapper;
import br.com.teste.sicredi.mapper.StatusMapper;
import br.com.teste.sicredi.repository.AssociadoRepository;
import br.com.teste.sicredi.representation.request.CadastrarAssociadoRequest;
import br.com.teste.sicredi.representation.response.StatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
public class AssociadoService {

    @Autowired
    private AssociadoRepository repository;

    @Autowired
    private CpfValidatorService cpfValidatorService;

    @Autowired
    private AssociadoMapper associadoMapper;

    @Autowired
    private StatusMapper statusMapper;

    public void cadastrarAssociado(CadastrarAssociadoRequest request) {

        Associado associado = associadoMapper.toDomain(request);

        log.info("Salvando associado criado.");

        repository.save(associado);
    }

    public StatusResponse consultaCpf(String cpf) {
        if (cpfValidatorService.verificaCpfValido(cpf)) {
            return randomizaRetorno();
        }
        return null;
    }

    public StatusResponse randomizaRetorno() {
        Random random = new Random();
        int resultado = random.nextInt(2);

        if (resultado == 0) {
            return statusMapper.toResponse(Status.ABLE_TO_VOTE.toString());
        }
        return statusMapper.toResponse(Status.UNABLE_TO_VOTE.toString());

    }

    public Optional<Associado> getById(Integer idAssociado) {
        return repository.findById(idAssociado);
    }
}
