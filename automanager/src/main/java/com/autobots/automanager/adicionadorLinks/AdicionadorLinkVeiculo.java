package com.autobots.automanager.adicionadorLinks;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ControleVeiculo;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.modelos.AdicionadorLink;

@Component
public class AdicionadorLinkVeiculo implements AdicionadorLink<Veiculo>{
	@Override
	public void adicionarLink(List<Veiculo> lista) {
		for (Veiculo objeto : lista) {
			long id = objeto.getId();
			Link linkProprioVeiculo = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleVeiculo.class)
							.obterVeiculo(id))
					.withSelfRel();
			Link linkProprioExcluir = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleVeiculo.class)
							.excluirVeiculo(objeto, 1))
					.withSelfRel();
			Link linkProprioAtualizar = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleVeiculo.class)
							.atualizarVeiculo(objeto))
					.withSelfRel();
			Link linkProprioCadastrar = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleVeiculo.class)
							.cadastrarVeiculo(objeto, 1))
					.withSelfRel();
			Link linkProprioVeiculos = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleVeiculo.class)
							.obterVeiculos())
					.withSelfRel();
			objeto.add(linkProprioVeiculo);
			objeto.add(linkProprioExcluir);
			objeto.add(linkProprioAtualizar);
			objeto.add(linkProprioCadastrar);
			objeto.add(linkProprioVeiculos);
		}
	}

	public void adicionarLink(Veiculo objeto) {
		Link linkProprioVeiculo = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleVeiculo.class)
						.obterVeiculo(objeto.getId()))
				.withRel("veiculo");
		Link linkProprioVeiculos = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleVeiculo.class)
						.obterVeiculos())
				.withRel("veiculos");
		Link linkProprioCadastrar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleVeiculo.class)
						.cadastrarVeiculo(objeto, 1))
				.withRel("cadastrar");
		Link linkProprioAtualizar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleVeiculo.class)
						.atualizarVeiculo(objeto))
				.withRel("atualizar");
		Link linkProprioExcluir = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleVeiculo.class)
						.excluirVeiculo(objeto, 1))
				.withRel("excluir");
		objeto.add(linkProprioVeiculo);
		objeto.add(linkProprioVeiculos);
		objeto.add(linkProprioCadastrar);
		objeto.add(linkProprioAtualizar);
		objeto.add(linkProprioExcluir);
	}
}