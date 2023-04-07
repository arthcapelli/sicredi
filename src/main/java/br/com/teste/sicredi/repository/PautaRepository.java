package br.com.teste.sicredi.repository;

import br.com.teste.sicredi.domain.Pauta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PautaRepository extends CrudRepository<Pauta, Integer> {
}
