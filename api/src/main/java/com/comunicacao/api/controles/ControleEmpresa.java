package com.comunicacao.api.controles;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.comunicacao.api.dto.EmpresaDto;

@RestController
public class ControleEmpresa {
	
    @GetMapping("/empresa")
    public ResponseEntity<?> obterEmpresas() {
        try {
            ResponseEntity<List<EmpresaDto>> resposta = new RestTemplate().exchange(
                "http://localhost:8080/empresa/listar",
                org.springframework.http.HttpMethod.GET,
                null,
                new org.springframework.core.ParameterizedTypeReference<List<EmpresaDto>>() {}
            );

            List<EmpresaDto> empresas = resposta.getBody();
            return new ResponseEntity<>(empresas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao obter empresas: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

   

}
