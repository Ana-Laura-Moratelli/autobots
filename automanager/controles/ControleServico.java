package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.adicionadorLinks.AdicionadorLinkServico;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.repositorios.RepositorioServico;
import com.autobots.automanager.repositorios.RepositorioEmpresa;

@RestController
@RequestMapping("/servico")
public class ControleServico {
	@Autowired
	private RepositorioServico repositorio;
	@Autowired
	private RepositorioEmpresa repositorioEmpresa;
	@Autowired
	private AdicionadorLinkServico adicionadorLink;

	@PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
	@GetMapping("/{id}")
	public ResponseEntity<Servico> obterServico(@PathVariable long id) {
	    List<Servico> servicos = repositorio.findAll();
	    Servico servico = null;

	    for (Servico s : servicos) {
	        if (s.getId() == id) {
	            servico = s;
	            break;
	        }
	    }

	    if (servico == null) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

	    adicionadorLink.adicionarLink(servico);
	    return new ResponseEntity<>(servico, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
	@GetMapping("/listar")
	public ResponseEntity<List<Servico>> obterServicos() {
		List<Servico> servicos = repositorio.findAll();
		if (servicos.isEmpty()) {
			ResponseEntity<List<Servico>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(servicos);
			ResponseEntity<List<Servico>> resposta = new ResponseEntity<>(servicos, HttpStatus.FOUND);
			return resposta;
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
	@PostMapping("/cadastrar/{id}")
	public ResponseEntity<?> cadastrarServico(@RequestBody Servico servico, @PathVariable long id) {
	    HttpStatus status = HttpStatus.CONFLICT;

	    if (servico.getId() == null) {
	        Optional<Empresa> optionalEmpresa = repositorioEmpresa.findById(id);
	        if (optionalEmpresa.isPresent()) {
	            Empresa empresa = optionalEmpresa.get();

	            Set<Servico> servicos = empresa.getServicos();
	            servicos.add(servico);
	            empresa.setServicos(servicos);

	            repositorioEmpresa.save(empresa);
	            status = HttpStatus.CREATED;
	        } else {
	            status = HttpStatus.NOT_FOUND;
	        }
	    }
	    return new ResponseEntity<>(status);
	}

	@PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarServico(@RequestBody Servico atualizacao) {
	    HttpStatus status = HttpStatus.BAD_REQUEST;

	    if (atualizacao.getId() != null) {
	        Servico servico = repositorio.findById(atualizacao.getId()).orElse(null);

	        if (servico != null) {
	            if (atualizacao.getNome() != null) {
	                servico.setNome(atualizacao.getNome());
	            }
	            if (atualizacao.getValor() > 0) {
	                servico.setValor(atualizacao.getValor());
	            }
	            if (atualizacao.getDescricao() != null) {
	                servico.setDescricao(atualizacao.getDescricao());
	            }

	            repositorio.save(servico);
	            status = HttpStatus.OK;
	        } else {
	            status = HttpStatus.NOT_FOUND;
	        }
	    }

	    return new ResponseEntity<>(status);
	}

	@PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirServico(@RequestBody Servico exclusao, @PathVariable long id) {
	    try {
	        Servico servico = repositorio.findById(exclusao.getId()).orElse(null);
	        if (servico == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Serviço não encontrado");
	        }

	        Empresa empresa = repositorioEmpresa.findById(id).orElse(null);
	        if (empresa == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não encontrada");
	        }

	        boolean removido = empresa.getServicos().removeIf(s -> s.getId().equals(servico.getId()));
	        if (!removido) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Serviço não está associado à empresa");
	        }

	        repositorioEmpresa.save(empresa);

	        repositorio.delete(servico);

	        return ResponseEntity.status(HttpStatus.OK).body("Serviço excluído com sucesso");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao excluir serviço");
	    }
	}



}