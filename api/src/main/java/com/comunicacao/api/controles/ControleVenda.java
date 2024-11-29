package com.comunicacao.api.controles;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.autobots.automanager.entidades.Venda;

@RestController
public class ControleVenda {
	
	 @GetMapping("/vendas")
	    public ResponseEntity<?> obterVendas() {
	        try {
	            ResponseEntity<List<Venda>> resposta = new RestTemplate().exchange(
	                "http://localhost:8080/venda/listar",
	                org.springframework.http.HttpMethod.GET,
	                null,
	                new org.springframework.core.ParameterizedTypeReference<List<Venda>>() {}
	            );

	            List<Venda> vendas = resposta.getBody();
	            return new ResponseEntity<>(vendas, HttpStatus.OK);
	        } catch (Exception e) {
	            return new ResponseEntity<>("Erro ao obter vendas: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

}