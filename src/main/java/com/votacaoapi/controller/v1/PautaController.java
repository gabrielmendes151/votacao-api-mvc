package com.votacaoapi.controller.v1;

import com.votacaoapi.request.AbrirSessaoRequest;
import com.votacaoapi.request.PautaRequest;
import com.votacaoapi.service.PautaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pautas")
public class PautaController {

    private final PautaService service;

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody PautaRequest request) {
        service.salvar(request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}/abrir-sessao")
    public ResponseEntity<?> abrirSessao(@PathVariable("id") Long id, @RequestBody AbrirSessaoRequest abrirSessaoRequest) {
        service.abrirSessao(id, abrirSessaoRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}/resultado-votacao")
    public ResponseEntity<String> fetchResultadoVotacao(@PathVariable("id") Long id) {
        var resultado = service.fetchResultadoVotacao(id);
        return ResponseEntity.ok(resultado);
    }
}
