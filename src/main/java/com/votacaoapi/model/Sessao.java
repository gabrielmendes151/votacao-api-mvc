package com.votacaoapi.model;

import com.votacaoapi.enums.SessaoStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SESSOES")
public class Sessao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime dataHorainicio;

    private LocalDateTime dataHorafim;

    @Enumerated(value = EnumType.STRING)
    private SessaoStatus status;

    public static Sessao criarSessao(final LocalDateTime inicio, final LocalDateTime fim) {
        return Sessao.builder()
            .status(sessaoDeveSerIniciada(inicio) ? SessaoStatus.INICIADA : SessaoStatus.ABERTA)
            .dataHorainicio(inicio)
            .dataHorafim(fim)
            .build();
    }
    public static Sessao criarSessaoDefault() {
        var now = LocalDateTime.now();
        return Sessao.builder()
            .status(SessaoStatus.INICIADA)
            .dataHorainicio(now)
            .dataHorafim(now.plusMinutes(1))
            .build();
    }

    private static Boolean sessaoDeveSerIniciada(final LocalDateTime inicio) {
        return inicio.isBefore(LocalDateTime.now());
    }
}

