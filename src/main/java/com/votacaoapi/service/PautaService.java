package com.votacaoapi.service;

import com.votacaoapi.Utils.DataUtils;
import com.votacaoapi.enums.SessaoStatus;
import com.votacaoapi.enums.Voto;
import com.votacaoapi.exceptions.NotFoundException;
import com.votacaoapi.exceptions.ValidacaoException;
import com.votacaoapi.model.Pauta;
import com.votacaoapi.model.Sessao;
import com.votacaoapi.repository.PautaRepository;
import com.votacaoapi.repository.PautasVotadasAssociadosRepository;
import com.votacaoapi.request.AbrirSessaoRequest;
import com.votacaoapi.request.PautaRequest;
import com.votacaoapi.response.VotacaoPautaResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PautaService {

    private final PautaRepository repository;

    private final SessaoService sessaoService;

    private final PautasVotadasAssociadosRepository pautasVotadasAssociadosRepository;

    public Pauta findById(final Long pautaId) {
        return repository.findById(pautaId)
            .orElseThrow(() -> new NotFoundException("Pauta não encontrada"));
    }

    @Transactional
    @CacheEvict(value = "pautas", allEntries = true)
    public void salvar(final PautaRequest pautaRequest) {
        repository.save(new Pauta(pautaRequest.getDescricao()));
    }

    @Transactional
    @CacheEvict(value = "pautas", allEntries = true)
    public void abrirSessao(final Long pautaId, final AbrirSessaoRequest abrirSessaoRequest) {
        if (abrirSessaoRequest == null || ObjectUtils.isEmpty(abrirSessaoRequest.getDataHorafim())
            || ObjectUtils.isEmpty(abrirSessaoRequest.getDataHorainicio())) {
            abrirSessao(pautaId);
        } else {
            var dataHoraInicio = DataUtils.convertStringToDateTime(abrirSessaoRequest.getDataHorainicio());
            var dataHoraFim = DataUtils.convertStringToDateTime(abrirSessaoRequest.getDataHorafim());
            abrirSessao(pautaId, dataHoraInicio, dataHoraFim);
        }
    }

    public void abrirSessao(final Long pautaId, final LocalDateTime inicio, final LocalDateTime fim) {
        var pauta = findById(pautaId);
        sessaoService.validar(inicio, fim);
        pauta.setSessao(Sessao.criarSessao(inicio, fim));
        repository.save(pauta);
    }

    public void abrirSessao(final Long pautaId) {
        Pauta pauta = findById(pautaId);
        pauta.setSessao(Sessao.criarSessaoDefault());
        repository.save(pauta);
    }

    @Cacheable("pautas")
    public Set<Pauta> findAllPautasComSessaoDiferenteDeFinalizada() {
        return repository.findAllBySessaoIsNotNullAndSessaoStatusIsNot(SessaoStatus.FINALIZADA);
    }

    public String fetchResultadoVotacao(final Long idPauta) {
        validarPauta(idPauta);
        var votosPauta = pautasVotadasAssociadosRepository.findAllByPautaId(idPauta);
        var votacaoPautaResponse = new VotacaoPautaResponse();
        votosPauta
            .forEach(vp -> {
                if (vp.getVoto() == Voto.SIM) {
                    votacaoPautaResponse.setVotosAfavor(votacaoPautaResponse.getVotosAfavor() + 1);
                } else {
                    votacaoPautaResponse.setVotosContra(votacaoPautaResponse.getVotosContra() + 1);
                }
            });
        return "Resultado da pauta " + votosPauta.get(0).getPauta().getDescricao() + " foi de "
            + fetchResultadoVotacao(votacaoPautaResponse) + " com um total de "
            + votacaoPautaResponse.getVotosAfavor() + "  votos a favor e um total de "
            + votacaoPautaResponse.getVotosContra() + "  votos contra";
    }

    private Pauta validarPauta(Long idPauta) {
        var pauta = findById(idPauta);
        if (ObjectUtils.isEmpty(pauta.getSessao())) {
            throw new NotFoundException("Não foi encontrada nenhuma sessão para essa pauta");
        }
        if (pauta.getSessao().getStatus() != SessaoStatus.FINALIZADA) {
            throw new ValidacaoException("Sessão não se encontra finalizada");
        }
        return pauta;
    }

    public String fetchResultadoVotacao(final VotacaoPautaResponse votacaoPautaResponse) {
        return votacaoPautaResponse.getVotosAfavor() > votacaoPautaResponse.getVotosContra()
            ? "APROVADA"
            : "NEGADA";
    }

}
