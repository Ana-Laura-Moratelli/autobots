package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.adicionadorLinks.AdicionadorLinkCredencial;
import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.repositorios.RepositorioCredencial;

@RestController
@RequestMapping("/credencial")
public class ControleCredencial {

    @Autowired
    private RepositorioCredencial repositorioCredencial;

 
    @Autowired
    private AdicionadorLinkCredencial adicionadorLink;

    @GetMapping("/{id}")
    public ResponseEntity<Credencial> obterCredencial(@PathVariable long id) {
        List<Credencial> credenciais = repositorioCredencial.findAll();
        Credencial credencial = credenciais.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
        if (credencial == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            adicionadorLink.adicionarLink(credencial);
            return new ResponseEntity<>(credencial, HttpStatus.OK);
        }
    }
    
}
