package com.autobots.automanager.adicionadorLinks;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ControleEndereco;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelos.AdicionadorLink;


@Component
public class AdicionadorLinkEndereco implements AdicionadorLink<Endereco>{

	@Override
	public void adicionarLink(List<Endereco> lista) {
		for (Endereco endereco : lista) {
			long id = endereco.getId();
			Link linkProprioEndereco = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleEndereco.class)
							.obterEndereco(id))
					.withSelfRel();
			Link linkProprioEnderecos = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleEndereco.class)
							.obterEnderecos())
					.withSelfRel();
			Link linkProprioCadastrar = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleEndereco.class)
							.cadastrarEndereco(endereco, 1))
					.withSelfRel();
			Link linkProprioAtualizar = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleEndereco.class)
							.atualizarEndereco(endereco))
					.withSelfRel();
			Link linkProprioExcluir = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleEndereco.class)
							.excluirEndereco(endereco, 1))
					.withSelfRel();
			endereco.add(linkProprioEndereco);
			endereco.add(linkProprioEnderecos);
			endereco.add(linkProprioCadastrar);
			endereco.add(linkProprioAtualizar);
			endereco.add(linkProprioExcluir);
		}
	}

	@Override
	public void adicionarLink(Endereco objeto) {
		Link linkProprioEnderecos = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleEndereco.class)
						.obterEnderecos())
				.withRel("enderecos");
		Link linkProprioEndereco = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleEndereco.class)
						.obterEndereco(objeto.getId()))
				.withRel("endereco");
		Link linkProprioCadastrar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleEndereco.class)
						.cadastrarEndereco(objeto, 1))
				.withRel("cadastrar");
		Link linkProprioAtualizar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleEndereco.class)
						.atualizarEndereco(objeto))
				.withRel("atualizar");
		Link linkProprioExcluir = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleEndereco.class)
						.excluirEndereco(objeto, 1))
				.withRel("excluir");
		objeto.add(linkProprioEndereco);
		objeto.add(linkProprioEnderecos);
		objeto.add(linkProprioCadastrar);
		objeto.add(linkProprioAtualizar);
		objeto.add(linkProprioExcluir);
	}
}