package com.autobots.automanager.adicionadorLinks;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ControleCredencialUsuarioSenha;
import com.autobots.automanager.entidades.CredencialUsuarioSenha;
import com.autobots.automanager.modelos.AdicionadorLink;

@Component
public class AdicionadorLinkCredencialUsuarioSenha implements AdicionadorLink<CredencialUsuarioSenha>{
	@Override
	public void adicionarLink(List<CredencialUsuarioSenha> lista) {
		for (CredencialUsuarioSenha objeto : lista) {
			long id = objeto.getId();
			Link linkProprioCredencial = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleCredencialUsuarioSenha.class)
							.obterCredencialUsuarioSenha(id))
					.withSelfRel();
			Link linkProprioExcluir = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleCredencialUsuarioSenha.class)
							.excluirCredencialUsuarioSenha(1))
					.withSelfRel();
			Link linkProprioAtualizar = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleCredencialUsuarioSenha.class)
							.atualizarCredencialUsuarioSenha(1, objeto))
					.withSelfRel();
			Link linkProprioCadastrar = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleCredencialUsuarioSenha.class)
							.cadastrarCredencialUsuarioSenha(objeto))
					.withSelfRel();
			Link linkProprioCredenciais = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleCredencialUsuarioSenha.class)
							.obterCredenciaisUsuarioSenha())
					.withSelfRel();
			objeto.add(linkProprioCredencial);
			objeto.add(linkProprioExcluir);
			objeto.add(linkProprioAtualizar);
			objeto.add(linkProprioCadastrar);
			objeto.add(linkProprioCredenciais);
		}
	}

	@Override
	public void adicionarLink(CredencialUsuarioSenha objeto) {
		Link linkProprioCredencial = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleCredencialUsuarioSenha.class)
						.obterCredencialUsuarioSenha(objeto.getId()))
				.withRel("credencialUsuario");
		Link linkProprioCredenciais = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleCredencialUsuarioSenha.class)
						.obterCredenciaisUsuarioSenha())
				.withRel("credenciaisUsuario");
		Link linkProprioCadastrar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleCredencialUsuarioSenha.class)
						.cadastrarCredencialUsuarioSenha(objeto))
				.withRel("cadastrar");
		Link linkProprioAtualizar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleCredencialUsuarioSenha.class)
						.atualizarCredencialUsuarioSenha(1, objeto))
				.withRel("atualizar");
		Link linkProprioExcluir = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleCredencialUsuarioSenha.class)
						.excluirCredencialUsuarioSenha(1))
				.withRel("excluir");
		objeto.add(linkProprioCredencial);
		objeto.add(linkProprioCredenciais);
		objeto.add(linkProprioCadastrar);
		objeto.add(linkProprioAtualizar);
		objeto.add(linkProprioExcluir);
	}
}