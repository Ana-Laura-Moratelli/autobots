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

import com.autobots.automanager.adicionadorLinks.AdicionadorLinkEmpresa;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.repositorios.RepositorioEmpresa;

@RestController
@RequestMapping("/empresa")
public class ControleEmpresa {
	@Autowired
	private RepositorioEmpresa repositorio;
	@Autowired
	private AdicionadorLinkEmpresa adicionadorLink;

	@GetMapping("/{id}")
	public ResponseEntity<Empresa> obterEmpresa(@PathVariable long id) {
	    List<Empresa> empresas = repositorio.findAll();
	    Empresa empresa = null;

	    for (Empresa e : empresas) {
	        if (e.getId() == id) {
	            empresa = e;
	            break;
	        }
	    }

	    if (empresa == null) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

	    adicionadorLink.adicionarLink(empresa);
	    return new ResponseEntity<>(empresa, HttpStatus.OK); 
	}


	@GetMapping("/listar")
	public ResponseEntity<List<Empresa>> obterEmpresas() {
		List<Empresa> empresas = repositorio.findAll();
		if (empresas.isEmpty()) {
			ResponseEntity<List<Empresa>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(empresas);
			ResponseEntity<List<Empresa>> resposta = new ResponseEntity<>(empresas, HttpStatus.FOUND);
			return resposta;
		}
	}

	 @PostMapping("/cadastrar")
	    public ResponseEntity<Empresa> cadastrarEmpresa(@RequestBody Empresa empresa) {
	        try {
	            
	            Empresa empresaSalva = repositorio.save(empresa);
	            	           
	            return ResponseEntity.status(HttpStatus.CREATED).body(empresaSalva);
	        } catch (Exception e) {
	            
	            e.printStackTrace();
	        
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	        }
	    }


	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarEmpresa(@RequestBody Empresa atualizacao) {
	    HttpStatus status = HttpStatus.BAD_REQUEST;

	    if (atualizacao.getId() != null) {
	        Empresa empresa = repositorio.findById(atualizacao.getId()).orElse(null);

	        if (empresa != null) {
	            if (atualizacao.getRazaoSocial() != null) {
	                empresa.setRazaoSocial(atualizacao.getRazaoSocial());
	            }
	            if (atualizacao.getNomeFantasia() != null) {
	                empresa.setNomeFantasia(atualizacao.getNomeFantasia());
	            }
	            if (atualizacao.getTelefones() != null && !atualizacao.getTelefones().isEmpty()) {
	                empresa.setTelefones(atualizacao.getTelefones());
	            }
	            if (atualizacao.getEndereco() != null) {
	                empresa.setEndereco(atualizacao.getEndereco());
	            }
	            if (atualizacao.getCadastro() != null) {
	                empresa.setCadastro(atualizacao.getCadastro());
	            }
	            if (atualizacao.getUsuarios() != null && !atualizacao.getUsuarios().isEmpty()) {
	                empresa.setUsuarios(atualizacao.getUsuarios());
	            }
	            if (atualizacao.getMercadorias() != null && !atualizacao.getMercadorias().isEmpty()) {
	                empresa.setMercadorias(atualizacao.getMercadorias());
	            }
	            if (atualizacao.getServicos() != null && !atualizacao.getServicos().isEmpty()) {
	                empresa.setServicos(atualizacao.getServicos());
	            }
	            if (atualizacao.getVendas() != null && !atualizacao.getVendas().isEmpty()) {
	                empresa.setVendas(atualizacao.getVendas());
	            }

	            repositorio.save(empresa);
	            status = HttpStatus.OK;
	        } else {
	            status = HttpStatus.NOT_FOUND;
	        }
	    }

	    return new ResponseEntity<>(status);
	}


	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirEmpresa(@PathVariable long id) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Empresa empresa = repositorio.getById(id);
		if (empresa != null) {
			repositorio.delete(empresa);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
}