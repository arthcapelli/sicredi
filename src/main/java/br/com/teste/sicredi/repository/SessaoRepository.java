package br.com.teste.sicredi.repository;

import br.com.teste.sicredi.domain.Sessao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessaoRepository extends CrudRepository<Sessao, Integer> {

    Sessao findByIdPauta(Integer idPauta);

    boolean existsByIdPauta(Integer idPauta);
}
