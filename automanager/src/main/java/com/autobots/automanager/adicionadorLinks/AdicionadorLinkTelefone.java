package com.autobots.automanager.adicionadorLinks;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ControleTelefone;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelos.AdicionadorLink;


@Component
public class AdicionadorLinkTelefone implements AdicionadorLink<Telefone>{

	@Override
	public void adicionarLink(List<Telefone> lista) {
		for (Telefone telefone : lista) {
			long id = telefone.getId();
			Link linkProprioTelefone = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleTelefone.class)
							.obterTelefone(id))
					.withSelfRel();
			Link linkProprioTelefones = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleTelefone.class)
							.obterTelefones())
					.withSelfRel();
			Link linkProprioCadastrar = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleTelefone.class)
							.cadastrarTelefone(telefone, 1))
					.withSelfRel();
			Link linkProprioAtualizar = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleTelefone.class)
							.atualizarTelefone(telefone))
					.withSelfRel();
			Link linkProprioExcluir = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleTelefone.class)
							.excluirTelefone(telefone, 1))
					.withSelfRel();
			telefone.add(linkProprioTelefone);
			telefone.add(linkProprioTelefones);
			telefone.add(linkProprioCadastrar);
			telefone.add(linkProprioAtualizar);
			telefone.add(linkProprioExcluir);
		}
	}

	@Override
	public void adicionarLink(Telefone objeto) {
		Link linkProprioTelefones = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleTelefone.class)
						.obterTelefones())
				.withRel("telefones");
		Link linkProprioTelefone = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleTelefone.class)
						.obterTelefone(objeto.getId()))
				.withRel("telefone");
		Link linkProprioCadastrar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleTelefone.class)
						.cadastrarTelefone(objeto, 1))
				.withRel("cadastrar");
		Link linkProprioAtualizar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleTelefone.class)
						.atualizarTelefone(objeto))
				.withRel("atualizar");
		Link linkProprioExcluir = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleTelefone.class)
						.excluirTelefone(objeto, 1))
				.withRel("excluir");
		objeto.add(linkProprioTelefone);
		objeto.add(linkProprioTelefones);
		objeto.add(linkProprioCadastrar);
		objeto.add(linkProprioAtualizar);
		objeto.add(linkProprioExcluir);
	}
}