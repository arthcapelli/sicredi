package br.com.teste.sicredi.repository;

import br.com.teste.sicredi.domain.Voto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoRepository extends CrudRepository<Voto, Integer> {

    boolean existsByIdAssociadoAndIdPauta(Integer idAssociado, Integer idPauta);

    Integer countByIdPauta(Integer idPauta);
}
