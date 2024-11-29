package com.autobots.automanager.adicionadorLinks;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ControleEmail;
import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.modelos.AdicionadorLink;

@Component
public class AdicionadorLinkEmail implements AdicionadorLink<Email>{
	@Override
	public void adicionarLink(List<Email> lista) {
		for (Email objeto : lista) {
			long id = objeto.getId();
			Link linkProprioEmail = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleEmail.class)
							.obterEmail(id))
					.withSelfRel();
			Link linkProprioExcluir = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleEmail.class)
							.excluirEmail(objeto, 1))
					.withSelfRel();
			Link linkProprioAtualizar = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleEmail.class)
							.atualizarEmail(objeto))
					.withSelfRel();
			Link linkProprioCadastrar = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleEmail.class)
							.cadastrarEmail(objeto, 1))
					.withSelfRel();
			Link linkProprioEmails = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleEmail.class)
							.obterEmails())
					.withSelfRel();
			objeto.add(linkProprioEmail);
			objeto.add(linkProprioExcluir);
			objeto.add(linkProprioAtualizar);
			objeto.add(linkProprioCadastrar);
			objeto.add(linkProprioEmails);
		}
	}

	@Override
	public void adicionarLink(Email objeto) {
		Link linkProprioCredencial = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleEmail.class)
						.obterEmail(objeto.getId()))
				.withRel("email");
		Link linkProprioCredenciais = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleEmail.class)
						.obterEmails())
				.withRel("emails");
		Link linkProprioCadastrar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleEmail.class)
						.cadastrarEmail(objeto, 1))
				.withRel("cadastrar");
		Link linkProprioAtualizar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleEmail.class)
						.atualizarEmail(objeto))
				.withRel("atualizar");
		Link linkProprioExcluir = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleEmail.class)
						.excluirEmail(objeto, 1))
				.withRel("excluir");
		objeto.add(linkProprioCredencial);
		objeto.add(linkProprioCredenciais);
		objeto.add(linkProprioCadastrar);
		objeto.add(linkProprioAtualizar);
		objeto.add(linkProprioExcluir);
	}
}