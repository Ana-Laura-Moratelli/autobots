package com.autobots.automanager.adicionadorLinks;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ControleCredencial;
import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.modelos.AdicionadorLink;

@Component
public class AdicionadorLinkCredencial implements AdicionadorLink<Credencial> {

    @Override
    public void adicionarLink(List<Credencial> lista) {
        for (Credencial objeto : lista) {
            long id = objeto.getId();
            
            Link linkProprioCredencial = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ControleCredencial.class)
                            .obterCredencial(id))
                    .withSelfRel();                                   
            objeto.add(linkProprioCredencial);
            
           
        }
    }

    @Override
    public void adicionarLink(Credencial objeto) {
        long id = objeto.getId();
        
        Link linkProprioCredencial = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ControleCredencial.class)
                        .obterCredencial(id))
                .withSelfRel();                         
        objeto.add(linkProprioCredencial);
    
    }
}
