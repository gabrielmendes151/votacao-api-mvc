package com.votacaoapi.controller.v1;

import com.votacaoapi.request.AssociadoRequest;
import com.votacaoapi.service.AssociadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/associados")
public class AssociadoController {

    private final AssociadoService service;

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody AssociadoRequest request) {
        service.salvar(request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}/votar-pauta/{idPauta}")
    public ResponseEntity<?> votarPauta(@PathVariable("id") Long id, @PathVariable("idPauta") Long idPauta,
                                        @RequestParam("voto") String voto) {
        service.votar(id, idPauta, voto);
        return ResponseEntity.noContent().build();
    }
}
