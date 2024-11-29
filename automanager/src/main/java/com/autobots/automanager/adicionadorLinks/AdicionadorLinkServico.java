package com.autobots.automanager.adicionadorLinks;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ControleServico;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.modelos.AdicionadorLink;

@Component
public class AdicionadorLinkServico implements AdicionadorLink<Servico>{
	@Override
	public void adicionarLink(List<Servico> lista) {
		for (Servico objeto : lista) {
			long id = objeto.getId();
			Link linkProprioServico = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleServico.class)
							.obterServico(id))
					.withSelfRel();
			Link linkProprioExcluir = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleServico.class)
							.excluirServico(objeto, 1))
					.withSelfRel();
			Link linkProprioAtualizar = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleServico.class)
							.atualizarServico(objeto))
					.withSelfRel();
			Link linkProprioCadastrar = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleServico.class)
							.cadastrarServico(objeto, 1))
					.withSelfRel();
			Link linkProprioServicos = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleServico.class)
							.obterServicos())
					.withSelfRel();
			objeto.add(linkProprioServico);
			objeto.add(linkProprioExcluir);
			objeto.add(linkProprioAtualizar);
			objeto.add(linkProprioCadastrar);
			objeto.add(linkProprioServicos);
		}
	}

	@Override
	public void adicionarLink(Servico objeto) {
		Link linkProprioServico = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleServico.class)
						.obterServico(objeto.getId()))
				.withRel("servico");
		Link linkProprioServicos = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleServico.class)
						.obterServicos())
				.withRel("servicos");
		Link linkProprioCadastrar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleServico.class)
						.cadastrarServico(objeto, 1))
				.withRel("cadastrar");
		Link linkProprioAtualizar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleServico.class)
						.atualizarServico(objeto))
				.withRel("atualizar");
		Link linkProprioExcluir = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleServico.class)
						.excluirServico(objeto, 1))
				.withRel("excluir");
		objeto.add(linkProprioServico);
		objeto.add(linkProprioServicos);
		objeto.add(linkProprioCadastrar);
		objeto.add(linkProprioAtualizar);
		objeto.add(linkProprioExcluir);
	}
}