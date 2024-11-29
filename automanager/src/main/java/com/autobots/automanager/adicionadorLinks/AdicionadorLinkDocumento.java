package com.autobots.automanager.adicionadorLinks;

import java.util.List;


import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ControleDocumento;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelos.AdicionadorLink;


@Component
public class AdicionadorLinkDocumento implements AdicionadorLink<Documento>{
	
	@Override
	public void adicionarLink(List<Documento> lista) {
		for (Documento documento : lista) {
			long id = documento.getId();
			Link linkProprioDocumento = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleDocumento.class)
							.obterDocumento(id))
					.withSelfRel();
			Link linkProprioDocumentos = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleDocumento.class)
							.obterDocumentos())
					.withSelfRel();
			Link linkProprioCadastrar = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleDocumento.class)
							.cadastrarDocumento(documento, 1))
					.withSelfRel();
			Link linkProprioAtualizar = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleDocumento.class)
							.atualizarDocumento(documento))
					.withSelfRel();
			Link linkProprioExcluir = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleDocumento.class)
							.excluirDocumento(documento, 1))
					.withSelfRel();
			documento.add(linkProprioDocumento);
			documento.add(linkProprioDocumentos);
			documento.add(linkProprioCadastrar);
			documento.add(linkProprioAtualizar);
			documento.add(linkProprioExcluir);
		}
	}

	@Override
	public void adicionarLink(Documento objeto) {
		Link linkProprioDocumentos = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleDocumento.class)
						.obterDocumentos())
				.withRel("documentos");
		Link linkProprioDocumento = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleDocumento.class)
						.obterDocumento(objeto.getId()))
				.withRel("documento");
		Link linkProprioCadastrar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleDocumento.class)
						.cadastrarDocumento(objeto, 1))
				.withRel("cadastrar");
		Link linkProprioAtualizar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleDocumento.class)
						.atualizarDocumento(objeto))
				.withRel("atualizar");
		Link linkProprioExcluir = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleDocumento.class)
						.excluirDocumento(objeto, 1))
				.withRel("excluir");
		objeto.add(linkProprioDocumentos);
		objeto.add(linkProprioDocumento);
		objeto.add(linkProprioCadastrar);
		objeto.add(linkProprioAtualizar);
		objeto.add(linkProprioExcluir);
	}
}