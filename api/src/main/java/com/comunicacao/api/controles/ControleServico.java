package com.comunicacao.api.controles;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.autobots.automanager.entidades.Servico;


@RestController
public class ControleServico { 

@GetMapping("/servicos")
    public ResponseEntity<?> obterServicos() {
        try {
            ResponseEntity<List<Servico>> resposta = new RestTemplate().exchange(
                "http://localhost:8080/servico/listar",
                org.springframework.http.HttpMethod.GET,
                null,
                new org.springframework.core.ParameterizedTypeReference<List<Servico>>() {}
            );

            List<Servico> servicos = resposta.getBody();
            return new ResponseEntity<>(servicos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao obter serviços: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
    