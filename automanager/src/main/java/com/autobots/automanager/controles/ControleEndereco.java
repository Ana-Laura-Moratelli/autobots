package com.autobots.automanager.controles;

import java.util.List;

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

import com.autobots.automanager.adicionadorLinks.AdicionadorLinkEndereco;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.repositorios.RepositorioEndereco;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@RestController
@RequestMapping("/endereco")
public class ControleEndereco {
	@Autowired
	private RepositorioEndereco repositorio;
	@Autowired
	private RepositorioUsuario repositorioUsuario;
	@Autowired
	private AdicionadorLinkEndereco adicionadorLink;

	@GetMapping("/{id}")
	public ResponseEntity<Endereco> obterEndereco(@PathVariable long id) {
	    List<Endereco> enderecos = repositorio.findAll();
	    Endereco endereco = null;

	    for (Endereco e : enderecos) {
	        if (e.getId() == id) {
	            endereco = e;
	            break;
	        }
	    }

	    if (endereco == null) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

	    adicionadorLink.adicionarLink(endereco);
	    return new ResponseEntity<>(endereco, HttpStatus.OK);
	}


	@GetMapping("/listar")
	public ResponseEntity<List<Endereco>> obterEnderecos() {
		List<Endereco> enderecos = repositorio.findAll();
		if (enderecos.isEmpty()) {
			ResponseEntity<List<Endereco>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND) ;
			return resposta;
		} else {
			adicionadorLink.adicionarLink(enderecos);
			ResponseEntity<List<Endereco>> resposta = new ResponseEntity<>(enderecos, HttpStatus.FOUND) ;
			return resposta;
		}
	}

	@PostMapping("/cadastrar/{id}")
	public ResponseEntity<?> cadastrarEndereco(@RequestBody Endereco endereco, @PathVariable long id) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (endereco != null) {
			Usuario usuario = repositorioUsuario.getById(id);
			usuario.setEndereco(endereco);
			repositorioUsuario.save(usuario);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
		
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarEndereco(@RequestBody Endereco atualizacao) {
	    HttpStatus status = HttpStatus.BAD_REQUEST;

	    if (atualizacao.getId() != null) {
	        Endereco endereco = repositorio.findById(atualizacao.getId()).orElse(null);

	        if (endereco != null) {
	            if (atualizacao.getEstado() != null) {
	                endereco.setEstado(atualizacao.getEstado());
	            }
	            if (atualizacao.getCidade() != null) {
	                endereco.setCidade(atualizacao.getCidade());
	            }
	            if (atualizacao.getBairro() != null) {
	                endereco.setBairro(atualizacao.getBairro());
	            }
	            if (atualizacao.getRua() != null) {
	                endereco.setRua(atualizacao.getRua());
	            }
	            if (atualizacao.getNumero() != null) {
	                endereco.setNumero(atualizacao.getNumero());
	            }
	            if (atualizacao.getCodigoPostal() != null) {
	                endereco.setCodigoPostal(atualizacao.getCodigoPostal());
	            }
	            if (atualizacao.getInformacoesAdicionais() != null) {
	                endereco.setInformacoesAdicionais(atualizacao.getInformacoesAdicionais());
	            }

	            repositorio.save(endereco);
	            status = HttpStatus.OK;
	        } else {
	            status = HttpStatus.NOT_FOUND;
	        }
	    }

	    return new ResponseEntity<>(status);
	}


	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirEndereco(@RequestBody Endereco exclusao, @PathVariable long id) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Endereco endereco = repositorio.getById(exclusao.getId());
		if (endereco != null) {
			Usuario usuario = repositorioUsuario.getById(id);
			usuario.setEndereco(null);
			repositorioUsuario.save(usuario);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
}