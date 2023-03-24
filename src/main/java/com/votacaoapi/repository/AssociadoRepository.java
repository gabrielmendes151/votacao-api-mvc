package com.votacaoapi.repository;

import com.votacaoapi.model.Associado;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociadoRepository extends CrudRepository<Associado, Long> {
}
