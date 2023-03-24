package com.votacaoapi.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VotacaoPautaResponse {
    private Integer votosAfavor = 0;

    private Integer votosContra = 0;
}
