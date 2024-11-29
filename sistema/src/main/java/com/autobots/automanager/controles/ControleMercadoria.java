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

import com.autobots.automanager.adicionadorLinks.AdicionadorLinkMercadoria;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioMercadoria;

@RestController
@RequestMapping("/mercadoria")
public class ControleMercadoria {
	@Autowired
	private RepositorioMercadoria repositorio;
	@Autowired
	private RepositorioEmpresa repositorioEmpresa;
	@Autowired
	private AdicionadorLinkMercadoria adicionadorLink;

	@PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
	@GetMapping("/{id}")
	public ResponseEntity<Mercadoria> obterMercadoria(@PathVariable long id) {
	    List<Mercadoria> mercadorias = repositorio.findAll();
	    Mercadoria mercadoria = null;

	    for (Mercadoria m : mercadorias) {
	        if (m.getId() == id) {
	            mercadoria = m;
	            break;
	        }
	    }

	    if (mercadoria == null) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

	    adicionadorLink.adicionarLink(mercadoria);
	    return new ResponseEntity<>(mercadoria, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
	@GetMapping("/listar")
	public ResponseEntity<List<Mercadoria>> obterMercadorias() {
		List<Mercadoria> Mercadorias = repositorio.findAll();
		if (Mercadorias.isEmpty()) {
			ResponseEntity<List<Mercadoria>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(Mercadorias);
			ResponseEntity<List<Mercadoria>> resposta = new ResponseEntity<>(Mercadorias, HttpStatus.FOUND);
			return resposta;
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
	@PostMapping("/cadastrar/{id}")
	public ResponseEntity<?> cadastrarMercadoria(@RequestBody Mercadoria mercadoria, @PathVariable long id) {
	    HttpStatus status = HttpStatus.CONFLICT;

	    if (mercadoria.getId() == null) {
	        Optional<Empresa> optionalEmpresa = repositorioEmpresa.findById(id);
	        if (optionalEmpresa.isPresent()) {
	            Empresa empresa = optionalEmpresa.get();

	            Set<Mercadoria> mercadorias = empresa.getMercadorias();
	            mercadorias.add(mercadoria);
	            empresa.setMercadorias(mercadorias);

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
	public ResponseEntity<?> atualizarMercadoria(@RequestBody Mercadoria atualizacao) {
	    HttpStatus status = HttpStatus.BAD_REQUEST;

	    if (atualizacao.getId() != null) {
	        Mercadoria mercadoria = repositorio.findById(atualizacao.getId()).orElse(null);

	        if (mercadoria != null) {
	            if (atualizacao.getValidade() != null) {
	                mercadoria.setValidade(atualizacao.getValidade());
	            }
	            if (atualizacao.getFabricacao() != null) {
	                mercadoria.setFabricacao(atualizacao.getFabricacao());
	            }
	            if (atualizacao.getCadastro() != null) {
	                mercadoria.setCadastro(atualizacao.getCadastro());
	            }
	            if (atualizacao.getNome() != null) {
	                mercadoria.setNome(atualizacao.getNome());
	            }
	            if (atualizacao.getQuantidade() > 0) {
	                mercadoria.setQuantidade(atualizacao.getQuantidade());
	            }
	            if (atualizacao.getValor() > 0) {
	                mercadoria.setValor(atualizacao.getValor());
	            }
	            if (atualizacao.getDescricao() != null) {
	                mercadoria.setDescricao(atualizacao.getDescricao());
	            }

	            repositorio.save(mercadoria);
	            status = HttpStatus.OK;
	        } else {
	            status = HttpStatus.NOT_FOUND;
	        }
	    }

	    return new ResponseEntity<>(status);
	}

	@PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirMercadoria(@RequestBody Mercadoria exclusao, @PathVariable long id) {
	    try {
	        Mercadoria mercadoria = repositorio.findById(exclusao.getId()).orElse(null);
	        if (mercadoria == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mercadoria não encontrada");
	        }

	        Empresa empresa = repositorioEmpresa.findById(id).orElse(null);
	        if (empresa == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não encontrada");
	        }

	        boolean mercadoriaRemovida = empresa.getMercadorias().removeIf(mer -> mer.getId().equals(exclusao.getId()));

	        if (!mercadoriaRemovida) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mercadoria não está associada à empresa");
	        }

	        repositorioEmpresa.save(empresa);

	        repositorio.delete(mercadoria);

	        return ResponseEntity.status(HttpStatus.OK).body("Mercadoria excluída com sucesso");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao excluir mercadoria");
	    }
	}

}