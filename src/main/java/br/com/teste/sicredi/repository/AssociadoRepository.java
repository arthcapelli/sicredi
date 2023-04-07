package br.com.teste.sicredi.repository;

import br.com.teste.sicredi.domain.Associado;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociadoRepository extends CrudRepository<Associado, Integer> {
}
