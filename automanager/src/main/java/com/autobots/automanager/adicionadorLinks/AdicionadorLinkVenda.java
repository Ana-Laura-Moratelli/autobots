package com.autobots.automanager.adicionadorLinks;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ControleVenda;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelos.AdicionadorLink;

@Component
public class AdicionadorLinkVenda implements AdicionadorLink<Venda> {
	
	@Override
	public void adicionarLink(List<Venda> lista) {
		for (Venda objeto : lista) {
			long id = objeto.getId();
			Link linkProprioVenda = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleVenda.class)
							.obterVenda(id))
					.withSelfRel();
			Link linkProprioExcluir = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleVenda.class)
							.excluirVenda(1))
					.withSelfRel();
			Link linkProprioAtualizar = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleVenda.class)
							.atualizarVenda(objeto))
					.withSelfRel();
			Link linkProprioCadastrar = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleVenda.class)
							.cadastrarVenda(objeto))
					.withSelfRel();
			Link linkProprioVendas = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleVenda.class)
							.obterVendas())
					.withSelfRel();
			objeto.add(linkProprioVenda);
			objeto.add(linkProprioExcluir);
			objeto.add(linkProprioAtualizar);
			objeto.add(linkProprioCadastrar);
			objeto.add(linkProprioVendas);
		}
	}

	@Override
	public void adicionarLink(Venda objeto) {
		Link linkProprioVenda = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleVenda.class)
						.obterVenda(objeto.getId()))
				.withRel("venda");
		Link linkProprioVendas = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleVenda.class)
						.obterVendas())
				.withRel("vendas");
		Link linkProprioCadastrar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleVenda.class)
						.cadastrarVenda(objeto))
				.withRel("cadastrar");
		Link linkProprioAtualizar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleVenda.class)
						.atualizarVenda(objeto))
				.withRel("atualizar");
		Link linkProprioExcluir = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleVenda.class)
						.excluirVenda(1))
				.withRel("excluir");
		objeto.add(linkProprioVenda);
		objeto.add(linkProprioVendas);
		objeto.add(linkProprioCadastrar);
		objeto.add(linkProprioAtualizar);
		objeto.add(linkProprioExcluir);
	}
}