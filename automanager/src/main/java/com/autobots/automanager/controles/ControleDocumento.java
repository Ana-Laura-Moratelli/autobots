package com.autobots.automanager.controles;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.adicionadorLinks.AdicionadorLinkDocumento;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioDocumento;

@RestController
@RequestMapping("/documento")
public class ControleDocumento {
	@Autowired
	private RepositorioDocumento repositorio;
	@Autowired
	private RepositorioUsuario repositorioUsuario;
	@Autowired
	private AdicionadorLinkDocumento adicionadorLink;

	@GetMapping("/{id}")
	public ResponseEntity<Documento> obterDocumento(@PathVariable long id) {
	    List<Documento> documentos = repositorio.findAll();
	    Documento documento = null;

	    for (Documento d : documentos) {
	        if (d.getId() == id) {
	            documento = d;
	            break;
	        }
	    }

	    if (documento == null) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

	    adicionadorLink.adicionarLink(documento);
	    return new ResponseEntity<>(documento, HttpStatus.OK);
	}


	@GetMapping("/listar")
	public ResponseEntity<List<Documento>> obterDocumentos() {
		List<Documento> documentos = repositorio.findAll();
		if (documentos.isEmpty()) {
			ResponseEntity<List<Documento>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(documentos);
			ResponseEntity<List<Documento>> resposta = new ResponseEntity<>(documentos, HttpStatus.FOUND);
			return resposta;
		}
	}

	@PostMapping("/cadastrar/{id}")
	public ResponseEntity<?> cadastrarDocumento(@RequestBody Documento documento, @PathVariable long id) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (documento.getId() == null) {
			Usuario usuario = repositorioUsuario.getById(id);
			Set<Documento> documentos = usuario.getDocumentos();
			documentos.add(documento);
			usuario.setDocumentos(documentos);
			repositorioUsuario.save(usuario);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarDocumento(@RequestBody Documento atualizacao) {
	    HttpStatus status = HttpStatus.BAD_REQUEST;

	    if (atualizacao.getId() != null) {
	        Documento documento = repositorio.findById(atualizacao.getId()).orElse(null);

	        if (documento != null) {
	            if (atualizacao.getTipo() != null) {
	                documento.setTipo(atualizacao.getTipo());
	            }
	            if (atualizacao.getDataEmissao() != null) {
	                documento.setDataEmissao(atualizacao.getDataEmissao());
	            }
	            if (atualizacao.getNumero() != null) {
	                documento.setNumero(atualizacao.getNumero());
	            }

	            repositorio.save(documento);
	            status = HttpStatus.OK;
	        } else {
	            status = HttpStatus.NOT_FOUND;
	        }
	    }

	    return new ResponseEntity<>(status);
	}


	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirDocumento(@RequestBody Documento exclusao, @PathVariable long id) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Documento documento = (Documento) repositorio.getById(exclusao.getId());
		if (documento != null) {
			Usuario usuario = repositorioUsuario.getById(id);
			Set<Documento> documentos = usuario.getDocumentos();
			for (Documento doc: documentos) {
				if (doc.getId() == exclusao.getId()) {
					documentos.remove(doc);
					break;
				}
			}
			usuario.setDocumentos(documentos);
			repositorioUsuario.save(usuario);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
}