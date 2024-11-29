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
                            .obterUsuario(id))
                    .withSelfRel();
            
            Link linkProprioExcluir = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ControleUsuario.class)
                            .excluirUsuario(1))
                    .withSelfRel();
            
            Link linkProprioCadastrar = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ControleUsuario.class)
                            .cadastrarUsuario(usuario, 1))
                    .withSelfRel();
            
            Link linkProprioUsuarios = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ControleUsuario.class)
                            .obterUsuarios())
                    .withSelfRel();
            
            Link linkProprioAtualizar = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ControleUsuario.class)
                            .atualizarUsuario(usuario))
                    .withSelfRel();

            usuario.add(linkProprioUsuario);
            usuario.add(linkProprioExcluir);
            usuario.add(linkProprioCadastrar);
            usuario.add(linkProprioUsuarios);
            usuario.add(linkProprioAtualizar);
        }
    }

    @Override
    public void adicionarLink(Usuario objeto) {
        Link linkProprioUsuario = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ControleUsuario.class)
                        .obterUsuario(objeto.getId()))
                .withRel("usuario");
        
        Link linkProprioUsuarios = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ControleUsuario.class)
                        .obterUsuarios())
                .withRel("usuarios");
        
        Link linkProprioCadastrar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ControleUsuario.class)
                        .cadastrarUsuario(objeto, 1))
                .withRel("cadastrar");
        
        Link linkProprioExcluir = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ControleUsuario.class)
                        .excluirUsuario(1))
                .withRel("excluir");
        
        Link linkProprioAtualizar = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ControleUsuario.class)
                        .atualizarUsuario(objeto))
                .withRel("atualizar");

        objeto.add(linkProprioUsuario);
        objeto.add(linkProprioUsuarios);
        objeto.add(linkProprioCadastrar);
        objeto.add(linkProprioExcluir);
        objeto.add(linkProprioAtualizar);
    }
}
