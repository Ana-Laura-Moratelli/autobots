package com.autobots.automanager.adicionadorLinks;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ContoleCredencialCodigoBarra;
import com.autobots.automanager.entidades.CredencialCodigoBarra;
import com.autobots.automanager.modelos.AdicionadorLink;

@Component
public class AdicionadorLinkCredencialCodigoBarra implements AdicionadorLink<CredencialCodigoBarra> {

    @Override
    public void adicionarLink(List<CredencialCodigoBarra> lista) {
        for (CredencialCodigoBarra credencial : lista) {
            long id = credencial.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ContoleCredencialCodigoBarra.class)
                            .obterCredencialCodigoBarra(id))
                    .withSelfRel();
            Link linkListar = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ContoleCredencialCodigoBarra.class)
                            .obterCredenciaisCodigoBarra())
                    .withSelfRel();
            Link linkCadastrar = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ContoleCredencialCodigoBarra.class)
                            .cadastrarCredencialCodigoBarra(credencial))
                    .withSelfRel();
            Link linkAtualizar = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ContoleCredencialCodigoBarra.class)
                            .atualizarCredencialCodigoBarra(id, credencial))
                    .withSelfRel();
            Link linkExcluir = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ContoleCredencialCodigoBarra.class)
                            .excluirCredencialCodigoBarra(id))
                    .withSelfRel();
            credencial.add(linkProprio);
            credencial.add(linkListar);
            credencial.add(linkCadastrar);
            credencial.add(linkAtualizar);
            credencial.add(linkExcluir);
        }
    }

    @Override
    public void adicionarLink(CredencialCodigoBarra objeto) {
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ContoleCredencialCodigoBarra.class)
                        .obterCredencialCodigoBarra(objeto.getId()))
                .withRel("credencial");
        Link linkListar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ContoleCredencialCodigoBarra.class)
                        .obterCredenciaisCodigoBarra())
                .withRel("credenciais");
        Link linkCadastrar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ContoleCredencialCodigoBarra.class)
                        .cadastrarCredencialCodigoBarra(objeto))
                .withRel("cadastrar");
        Link linkAtualizar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ContoleCredencialCodigoBarra.class)
                        .atualizarCredencialCodigoBarra(objeto.getId(), objeto))
                .withRel("atualizar");
        Link linkExcluir = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ContoleCredencialCodigoBarra.class)
                        .excluirCredencialCodigoBarra(objeto.getId()))
                .withRel("excluir");
        objeto.add(linkProprio);
        objeto.add(linkListar);
        objeto.add(linkCadastrar);
        objeto.add(linkAtualizar);
        objeto.add(linkExcluir);
    }
}
