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

import com.autobots.automanager.adicionadorLinks.AdicionadorLinkEmail;
import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.repositorios.RepositorioEmail;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@RestController
@RequestMapping("/email")
public class ControleEmail {
	@Autowired
	private RepositorioEmail repositorio;
	@Autowired
	private RepositorioUsuario repositorioUsuario;
	@Autowired
	private AdicionadorLinkEmail adicionadorLink;

	@GetMapping("/{id}")
	public ResponseEntity<Email> obterEmail(@PathVariable long id) {
	    List<Email> emails = repositorio.findAll();
	    Email email = null;

	    for (Email e : emails) {
	        if (e.getId() == id) {
	            email = e;
	            break;
	        }
	    }

	    if (email == null) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

	    adicionadorLink.adicionarLink(email);
	    return new ResponseEntity<>(email, HttpStatus.OK);
	}


	@GetMapping("/listar")
	public ResponseEntity<List<Email>> obterEmails() {
		List<Email> emails = repositorio.findAll();
		if (emails.isEmpty()) {
			ResponseEntity<List<Email>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(emails);
			ResponseEntity<List<Email>> resposta = new ResponseEntity<>(emails, HttpStatus.FOUND);
			return resposta;
		}
	}

	@PostMapping("/cadastrar/{id}")
	public ResponseEntity<?> cadastrarEmail(@RequestBody Email email, @PathVariable long id) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (email.getId() == null) {
			Usuario usuario = repositorioUsuario.getById(id);
			Set<Email> emails = usuario.getEmails();
			emails.add(email);
			usuario.setEmails(emails);
			repositorioUsuario.save(usuario);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarEmail(@RequestBody Email atualizacao) {
	    HttpStatus status = HttpStatus.BAD_REQUEST;

	    if (atualizacao.getId() != null) {
	        Email email = repositorio.findById(atualizacao.getId()).orElse(null);

	        if (email != null) {
	            if (atualizacao.getEndereco() != null) {
	                email.setEndereco(atualizacao.getEndereco());
	            }

	            repositorio.save(email);
	            status = HttpStatus.OK;
	        } else {
	            status = HttpStatus.NOT_FOUND;
	        }
	    }

	    return new ResponseEntity<>(status);
	}


	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirEmail(@RequestBody Email exclusao, @PathVariable long id) {
	    try {
	        Email email = repositorio.findById(exclusao.getId()).orElse(null);
	        if (email == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email não encontrado");
	        }

	        Usuario usuario = repositorioUsuario.findById(id).orElse(null);
	        if (usuario == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
	        }

	        boolean emailRemovido = usuario.getEmails().removeIf(e -> e.getId().equals(exclusao.getId()));

	        if (!emailRemovido) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email não está associado ao usuário");
	        }

	        repositorioUsuario.save(usuario);

	        repositorio.delete(email);

	        return ResponseEntity.status(HttpStatus.OK).body("Email excluído com sucesso");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao excluir email");
	    }
	}

}