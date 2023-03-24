package com.votacaoapi.service;

import com.votacaoapi.exceptions.ValidacaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SessaoService {

    public void validar(final LocalDateTime inicio, final LocalDateTime fim) {
        if (fim.isBefore(LocalDateTime.now())) {
            throw new ValidacaoException("A data hora final da sessão não pode ser menor que a data hora atual");
        }
        if (inicio.isAfter(fim)) {
            throw new ValidacaoException("A data hora final da sessão não pode ser menor que a data hora inicio");
        }
    }
}
