package com.comunicacao.api.controles;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.autobots.automanager.entidades.Veiculo;

@RestController
public class ControleVeiculo {
	
	@GetMapping("/veiculos")
	public ResponseEntity<?> obterVeiculos() {
	    try {
	        ResponseEntity<List<Veiculo>> resposta = new RestTemplate().exchange(
	            "http://localhost:8080/veiculo/listar",
	            org.springframework.http.HttpMethod.GET,
	            null,
	            new org.springframework.core.ParameterizedTypeReference<List<Veiculo>>() {}
	        );
	
	        List<Veiculo> veiculos = resposta.getBody();
	        return new ResponseEntity<>(veiculos, HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>("Erro ao obter veículos: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
}