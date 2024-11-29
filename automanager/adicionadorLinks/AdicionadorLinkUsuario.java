package com.autobots.automanager.adicionadorLinks;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ControleUsuario;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelos.AdicionadorLink;

@Component
public class AdicionadorLinkUsuario implements AdicionadorLink<Usuario> {

    @Override
    public void adicionarLink(List<Usuario> lista) {
        for (Usuario usuario : lista) {
            long id = usuario.getId();

            Link linkProprioUsuario = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ControleUsuario.class)
                            .obterUsuarioPorId(id))
                    .withSelfRel();

            Link linkProprioUsuarios = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ControleUsuario.class)
                            .obterUsuarios())
                    .withRel("listar");

            Link linkProprioCadastrar = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ControleUsuario.class)
                            .cadastrarUsuario(null, null))
                    .withSelfRel();
            Link linkProprioAtualizar = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ControleUsuario.class)
                            .atualizarUsuario(null))
                    .withSelfRel();

            Link linkProprioExcluir = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ControleUsuario.class)
                            .excluirUsuario(id))
                    .withRel("excluir");

            usuario.add(linkProprioUsuario);
            usuario.add(linkProprioUsuarios);
            usuario.add(linkProprioCadastrar);
            usuario.add(linkProprioAtualizar);
            usuario.add(linkProprioExcluir);
        }
    }

    @Override
    public void adicionarLink(Usuario objeto) {
        long id = objeto.getId();

        Link linkProprioUsuarios = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ControleUsuario.class)
                        .obterUsuarios())
                .withRel("listar");

        Link linkProprioUsuario = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ControleUsuario.class)
                        .obterUsuarioPorId(id))
                .withSelfRel();

        
        Link linkProprioCadastrar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ControleUsuario.class)
                        .cadastrarUsuario(null, null))
                .withRel("cadastrar");

        Link linkProprioAtualizar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ControleUsuario.class)
                        .atualizarUsuario(null))
                .withRel("atualizar");

        Link linkProprioExcluir = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ControleUsuario.class)
                        .excluirUsuario(id))
                .withRel("excluir");

        objeto.add(linkProprioUsuario);
        objeto.add(linkProprioUsuarios);
        objeto.add(linkProprioCadastrar);
        objeto.add(linkProprioAtualizar);
        objeto.add(linkProprioExcluir);
    }
}
