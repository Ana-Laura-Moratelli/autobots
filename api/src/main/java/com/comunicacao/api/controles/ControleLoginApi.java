package com.comunicacao.api.controles;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.comunicacao.api.dto.LoginDto;
import com.comunicacao.api.dto.TokenDto;

@RestController
@RequestMapping("/apilogin")
public class ControleLoginApi {

	@PostMapping
	public ResponseEntity<?> autenticar(@RequestBody LoginDto loginDto) {
	    try {
	        RestTemplate restTemplate = new RestTemplate();

	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Content-Type", "application/json"); 

	        HttpEntity<LoginDto> request = new HttpEntity<>(loginDto, headers);
	        ResponseEntity<TokenDto> resposta = restTemplate.postForEntity(
	            "http://localhost:8080/login", 
	            request,
	            TokenDto.class
	        );

	        return ResponseEntity.ok(resposta.getBody());
	    } catch (Exception ex) {
	        return ResponseEntity.status(401).body("Erro ao autenticar: " + ex.getMessage());
	    }
	}

}
