package com.comunicacao.api.controles;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.comunicacao.api.modelos.Usuario;

@RestController
public class ControleUsuario {
	
    @GetMapping("/usuario")
    public ResponseEntity<?> obterUsuarios() {
        try {
            ResponseEntity<List<Usuario>> resposta = new RestTemplate().exchange(
                "http://localhost:8080/usuario/listar", 
                org.springframework.http.HttpMethod.GET,
                null,
                new org.springframework.core.ParameterizedTypeReference<List<Usuario>>() {}
            );

            List<Usuario> usuarios = resposta.getBody();
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao obter usuários: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
}
