package com.comunicacao.api.controles;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.autobots.automanager.entidades.Mercadoria;


@RestController
public class ControleMercadoria {
	
	 @GetMapping("/mercadorias")
	    public ResponseEntity<?> obterMercadorias() {
	        try {
	            ResponseEntity<List<Mercadoria>> resposta = new RestTemplate().exchange(
	                "http://localhost:8080/mercadoria/listar",
	                org.springframework.http.HttpMethod.GET,
	                null,
	                new org.springframework.core.ParameterizedTypeReference<List<Mercadoria>>() {}
	            );

	            List<Mercadoria> mercadorias = resposta.getBody();
	            return new ResponseEntity<>(mercadorias, HttpStatus.OK);
	        } catch (Exception e) {
	            return new ResponseEntity<>("Erro ao obter mercadorias: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

}