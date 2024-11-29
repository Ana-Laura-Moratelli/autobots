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

import com.autobots.automanager.adicionadorLinks.AdicionadorLinkTelefone;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.repositorios.RepositorioTelefone;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@RestController
@RequestMapping("/telefone")
public class ControleTelefone {
	@Autowired
	private RepositorioTelefone repositorio;
	@Autowired
	private RepositorioUsuario repositorioUsuario;
	@Autowired
	private AdicionadorLinkTelefone adicionadorLink;

	@GetMapping("/{id}")
	public ResponseEntity<Telefone> obterTelefone(@PathVariable long id) {
	    List<Telefone> telefones = repositorio.findAll();
	    Telefone telefone = null;

	    for (Telefone t : telefones) {
	        if (t.getId() == id) {
	            telefone = t;
	            break;
	        }
	    }

	    if (telefone == null) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

	    adicionadorLink.adicionarLink(telefone);
	    return new ResponseEntity<>(telefone, HttpStatus.OK);
	}


	@GetMapping("/listar")
	public ResponseEntity<List<Telefone>> obterTelefones() {
		List<Telefone> telefones = repositorio.findAll();
		if (telefones.isEmpty()) {
			ResponseEntity<List<Telefone>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(telefones);
			ResponseEntity<List<Telefone>> resposta = new ResponseEntity<>(telefones, HttpStatus.FOUND);
			return resposta;
		}
	}

	@PostMapping("/cadastrar/{id}")
	public ResponseEntity<?> cadastrarTelefone(@RequestBody Telefone telefone, @PathVariable long id) {
	    try {
	        
	        Usuario usuario = repositorioUsuario.findById(id).orElse(null);
	        if (usuario == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
	        }
	        
	        usuario.getTelefones().add(telefone);
        
	        repositorioUsuario.save(usuario);
	   
	        return ResponseEntity.status(HttpStatus.CREATED).body("Telefone cadastrado com sucesso");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cadastrar telefone");
	    }
	}


	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarTelefone(@RequestBody Telefone atualizacao) {
	    HttpStatus status = HttpStatus.BAD_REQUEST;

	    if (atualizacao.getId() != null) {
	        Telefone telefone = repositorio.findById(atualizacao.getId()).orElse(null);

	        if (telefone != null) {
	            if (atualizacao.getDdd() != null) {
	                telefone.setDdd(atualizacao.getDdd());
	            }
	            if (atualizacao.getNumero() != null) {
	                telefone.setNumero(atualizacao.getNumero());
	            }

	            repositorio.save(telefone);
	            status = HttpStatus.OK;
	        } else {
	            status = HttpStatus.NOT_FOUND;
	        }
	    }

	    return new ResponseEntity<>(status);
	}


	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirTelefone(@RequestBody Telefone exclusao, @PathVariable long id) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Telefone telefone = repositorio.getById(exclusao.getId());
		if (telefone != null) {
			Usuario usuario = repositorioUsuario.getById(id);
			Set<Telefone> telefones = usuario.getTelefones();
			for (Telefone tel: telefones) {
				if (tel.getId() == exclusao.getId()) {
					telefones.remove(tel);
					break;
				}
			}
			usuario.setTelefones(telefones);
			repositorioUsuario.save(usuario);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
}