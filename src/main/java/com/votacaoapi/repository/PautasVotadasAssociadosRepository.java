package com.votacaoapi.repository;

import com.votacaoapi.model.PautasVotadasAssociados;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PautasVotadasAssociadosRepository extends CrudRepository<PautasVotadasAssociados, Long> {

    List<PautasVotadasAssociadosRepository> findAllByAssociadoIdAndPautaId(Long associadoId, Long pautaId);

    List<PautasVotadasAssociados> findAllByPautaId(Long pautaId);
}
