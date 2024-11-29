package com.autobots.automanager.controles;

import java.util.List;
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

import com.autobots.automanager.adicionadorLinks.AdicionadorLinkVeiculo;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVeiculo;

@RestController
@RequestMapping("/veiculo")
public class ControleVeiculo {
	@Autowired
	private RepositorioVeiculo repositorio;
	@Autowired
	private RepositorioUsuario repositorioUsuario;
	@Autowired
	private AdicionadorLinkVeiculo adicionadorLink;

	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<Veiculo> obterVeiculo(@PathVariable long id) {
	    List<Veiculo> veiculos = repositorio.findAll();
	    Veiculo veiculo = null;

	    for (Veiculo v : veiculos) {
	        if (v.getId() == id) {
	            veiculo = v;
	            break;
	        }
	    }

	    if (veiculo == null) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

	    adicionadorLink.adicionarLink(veiculo);
	    return new ResponseEntity<>(veiculo, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/listar")
	public ResponseEntity<List<Veiculo>> obterVeiculos() {
		List<Veiculo> veiculos = repositorio.findAll();
		if (veiculos.isEmpty()) {
			ResponseEntity<List<Veiculo>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(veiculos);
			ResponseEntity<List<Veiculo>> resposta = new ResponseEntity<>(veiculos, HttpStatus.FOUND);
			return resposta;
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/cadastrar/{id}")
	public ResponseEntity<?> cadastrarVeiculo(@RequestBody Veiculo veiculo, @PathVariable long id) {
	    try {
	        if (veiculo.getId() == null) {
	            Usuario usuario = repositorioUsuario.findById(id).orElse(null);
	            if (usuario == null) {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
	            }

	            veiculo.setProprietario(usuario);

	            usuario.getVeiculos().add(veiculo);

	            repositorioUsuario.save(usuario);

	            return ResponseEntity.status(HttpStatus.CREATED).body("Veículo cadastrado com sucesso");
	        } else {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID do veículo já existe");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cadastrar veículo");
	    }
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarVeiculo(@RequestBody Veiculo atualizacao) {
	    HttpStatus status = HttpStatus.BAD_REQUEST;

	    if (atualizacao.getId() != null) {
	        Veiculo veiculo = repositorio.findById(atualizacao.getId()).orElse(null);

	        if (veiculo != null) {
	            if (atualizacao.getTipo() != null) {
	                veiculo.setTipo(atualizacao.getTipo());
	            }
	            if (atualizacao.getModelo() != null) {
	                veiculo.setModelo(atualizacao.getModelo());
	            }
	            if (atualizacao.getPlaca() != null) {
	                veiculo.setPlaca(atualizacao.getPlaca());
	            }
	            if (atualizacao.getProprietario() != null) {
	                veiculo.setProprietario(atualizacao.getProprietario());
	            }
	            if (atualizacao.getVendas() != null && !atualizacao.getVendas().isEmpty()) {
	                veiculo.setVendas(atualizacao.getVendas());
	            }

	            repositorio.save(veiculo);
	            status = HttpStatus.OK;
	        } else {
	            status = HttpStatus.NOT_FOUND;
	        }
	    }

	    return new ResponseEntity<>(status);
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirVeiculo(@RequestBody Veiculo exclusao, @PathVariable long id) {
	    try {
	        Veiculo veiculo = repositorio.findById(exclusao.getId()).orElse(null);
	        if (veiculo == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veículo não encontrado");
	        }

	        Usuario usuario = repositorioUsuario.findById(id).orElse(null);
	        if (usuario == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
	        }

	        Set<Veiculo> veiculos = usuario.getVeiculos();
	        boolean removido = veiculos.removeIf(vei -> vei.getId().equals(exclusao.getId()));
	        if (!removido) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veículo não está associado ao usuário");
	        }

	        usuario.setVeiculos(veiculos);
	        repositorioUsuario.save(usuario);

	        repositorio.delete(veiculo);

	        return ResponseEntity.status(HttpStatus.OK).body("Veículo excluído com sucesso");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao excluir veículo");
	    }
	}

}