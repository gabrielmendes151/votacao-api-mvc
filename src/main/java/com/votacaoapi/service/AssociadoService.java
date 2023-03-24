package com.votacaoapi.service;

import com.votacaoapi.enums.SessaoStatus;
import com.votacaoapi.enums.Voto;
import com.votacaoapi.exceptions.NotFoundException;
import com.votacaoapi.exceptions.SessaoFinalizadaException;
import com.votacaoapi.exceptions.ValidacaoException;
import com.votacaoapi.model.Associado;
import com.votacaoapi.model.Pauta;
import com.votacaoapi.model.PautasVotadasAssociados;
import com.votacaoapi.repository.AssociadoRepository;
import com.votacaoapi.repository.PautasVotadasAssociadosRepository;
import com.votacaoapi.request.AssociadoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssociadoService {

    private final AssociadoRepository repository;

    private final PautaService pautaService;

    private final PautasVotadasAssociadosRepository pautasVotadasAssociadosRepository;

    private Associado findById(final Long associadoId) {
        return repository.findById(associadoId)
            .orElseThrow(() -> new NotFoundException("Associado não encontrado"));
    }

    public void salvar(final AssociadoRequest associadoRequest) {
        repository.save(new Associado(associadoRequest.getNome()));
    }

    public void votar(final Long idAssociado, final Long idPauta, final String voto) {
        var associado = findById(idAssociado);
        var pauta = pautaService.findById(idPauta);
        validar(pauta, associado);
        var pautaVotada = PautasVotadasAssociados.builder()
            .voto(Voto.findByDescricao(voto))
            .pauta(pauta)
            .associado(associado)
            .build();
        pautasVotadasAssociadosRepository.save(pautaVotada);

    }

    private void validar(final Pauta pauta, final Associado associado) {
        if (pauta.getSessao().getStatus() == SessaoStatus.FINALIZADA) {
            throw new SessaoFinalizadaException();
        }
        if (!pautasVotadasAssociadosRepository.findAllByAssociadoIdAndPautaId(associado.getId(), pauta.getId()).isEmpty()) {
            throw new ValidacaoException("Associado já votou nessa pauta");
        }
    }
}
