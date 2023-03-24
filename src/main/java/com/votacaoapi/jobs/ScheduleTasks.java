package com.votacaoapi.jobs;

import com.votacaoapi.enums.SessaoStatus;
import com.votacaoapi.model.Pauta;
import com.votacaoapi.model.Sessao;
import com.votacaoapi.repository.PautaRepository;
import com.votacaoapi.service.PautaService;
import com.votacaoapi.service.SessaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Component
public class ScheduleTasks {

    @Autowired
    private SessaoService sessaoService;

    @Autowired
    private PautaService pautaService;

    @Autowired
    private PautaRepository pautaRepository;

    @Scheduled(cron = "*/1 * * * * *")
    public void verificarSessao() {
        Set<Pauta> pautas = pautaService.findAllPautasComSessaoDiferenteDeFinalizada();
        pautas.forEach(pauta -> {
            if (deveIniciarSessao(pauta.getSessao())) {
                pauta.getSessao().setStatus(SessaoStatus.INICIADA);
            }
            if (deveFinalizarSessao(pauta.getSessao())) {
                pauta.getSessao().setStatus(SessaoStatus.FINALIZADA);
                //todo mandar mensagem para o kafka
            }
            pautaRepository.save(pauta);
        });
    }

    private Boolean deveIniciarSessao(final Sessao sessao) {
        return sessao.getDataHorainicio().isBefore(LocalDateTime.now())
            && LocalDateTime.now().isBefore(sessao.getDataHorafim());

    }

    private Boolean deveFinalizarSessao(final Sessao sessao) {
        return sessao.getDataHorafim().isBefore(LocalDateTime.now());

    }
}
