package com.autobots.automanager.adicionadorLinks;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ControleMercadoria;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.modelos.AdicionadorLink;

@Component
public class AdicionadorLinkMercadoria implements AdicionadorLink<Mercadoria>{
	@Override
	public void adicionarLink(List<Mercadoria> lista) {
		for (Mercadoria objeto : lista) {
			long id = objeto.getId();
			Link linkProprioMercadoria = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleMercadoria.class)
							.obterMercadoria(id))
					.withSelfRel();
			Link linkProprioExcluir = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleMercadoria.class)
							.excluirMercadoria(objeto, 1))
					.withSelfRel();
			Link linkProprioAtualizar = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleMercadoria.class)
							.atualizarMercadoria(objeto))
					.withSelfRel();
			Link linkProprioCadastrar = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleMercadoria.class)
							.cadastrarMercadoria(objeto, 1))
					.withSelfRel();
			Link linkProprioMercadorias = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleMercadoria.class)
							.obterMercadorias())
					.withSelfRel();
			objeto.add(linkProprioMercadoria);
			objeto.add(linkProprioExcluir);
			objeto.add(linkProprioAtualizar);
			objeto.add(linkProprioCadastrar);
			objeto.add(linkProprioMercadorias);
		}
	}

	@Override
	public void adicionarLink(Mercadoria objeto) {
		Link linkProprioMercadoria = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleMercadoria.class)
						.obterMercadoria(objeto.getId()))
				.withRel("empresa");
		Link linkProprioMercadorias = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleMercadoria.class)
						.obterMercadorias())
				.withRel("empresas");
		Link linkProprioCadastrar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleMercadoria.class)
						.cadastrarMercadoria(objeto, 1))
				.withRel("cadastrar");
		Link linkProprioAtualizar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleMercadoria.class)
						.atualizarMercadoria(objeto))
				.withRel("atualizar");
		Link linkProprioExcluir = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleMercadoria.class)
						.excluirMercadoria(objeto, 1))
				.withRel("excluir");
		objeto.add(linkProprioMercadoria);
		objeto.add(linkProprioMercadorias);
		objeto.add(linkProprioCadastrar);
		objeto.add(linkProprioAtualizar);
		objeto.add(linkProprioExcluir);
	}
}