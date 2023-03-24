package com.votacaoapi.repository;

import com.votacaoapi.enums.SessaoStatus;
import com.votacaoapi.model.Pauta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PautaRepository extends CrudRepository<Pauta, Long> {
    Set<Pauta> findAllBySessaoIsNotNullAndSessaoStatusIsNot(SessaoStatus sessaoStatus);
}
