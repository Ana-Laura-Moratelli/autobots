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

import com.autobots.automanager.adicionadorLinks.AdicionadorLinkUsuario;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVeiculo;
import com.autobots.automanager.repositorios.RepositorioVenda;
import com.autobots.automanager.repositorios.RepositorioEmpresa;

@RestController
@RequestMapping("/usuario")
public class ControleUsuario {
	@Autowired
	private RepositorioUsuario repositorio;
	@Autowired
	private RepositorioEmpresa repositorioEmpresa;
	@Autowired
	private RepositorioVeiculo repositorioVeiculo;
	@Autowired
	private RepositorioVenda repositorioVenda;
	@Autowired
	private AdicionadorLinkUsuario adicionadorLink;

	@GetMapping("/{id}")
	public ResponseEntity<Usuario> obterUsuario(@PathVariable long id) {
	    List<Usuario> usuarios = repositorio.findAll();
	    Usuario usuario = null;

	    for (Usuario u : usuarios) {
	        if (u.getId() == id) {
	            usuario = u;
	            break;
	        }
	    }

	    if (usuario == null) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

	    adicionadorLink.adicionarLink(usuario);
	    return new ResponseEntity<>(usuario, HttpStatus.OK);
	}

	@GetMapping("/listar")
	public ResponseEntity<List<Usuario>> obterUsuarios() {
		List<Usuario> usuarios = repositorio.findAll();
		if (usuarios.isEmpty()) {
			ResponseEntity<List<Usuario>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(usuarios);
			ResponseEntity<List<Usuario>> resposta = new ResponseEntity<>(usuarios, HttpStatus.FOUND);
			return resposta;
		}
	}

	@PostMapping("/cadastrar/{id}")
	public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario, @PathVariable long id) {
	    HttpStatus status = HttpStatus.CONFLICT;

	    if (usuario.getId() == null) {
	        Empresa empresa = repositorioEmpresa.findById(id).orElse(null);

	        if (empresa != null) {
	           
	            usuario.setEmpresa(empresa);
	          
	            usuario = repositorio.save(usuario);
	           
	            empresa.getUsuarios().add(usuario);
	            repositorioEmpresa.save(empresa);

	            status = HttpStatus.CREATED;
	        } else {
	            status = HttpStatus.NOT_FOUND; 
	        }
	    }

	    return new ResponseEntity<>(status);
	}



	
	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarUsuario(@RequestBody Usuario atualizacao) {
	    HttpStatus status = HttpStatus.BAD_REQUEST;

	    if (atualizacao.getId() != null) {
	        Usuario usuario = repositorio.findById(atualizacao.getId()).orElse(null);

	        if (usuario != null) {
	            if (atualizacao.getNome() != null) {
	                usuario.setNome(atualizacao.getNome());
	            }
	            if (atualizacao.getNomeSocial() != null) {
	                usuario.setNomeSocial(atualizacao.getNomeSocial());
	            }
	            if (atualizacao.getTelefones() != null && !atualizacao.getTelefones().isEmpty()) {
	                usuario.setTelefones(atualizacao.getTelefones());
	            }
	            if (atualizacao.getEndereco() != null) {
	                usuario.setEndereco(atualizacao.getEndereco());
	            }
	            if (atualizacao.getDocumentos() != null && !atualizacao.getDocumentos().isEmpty()) {
	                usuario.setDocumentos(atualizacao.getDocumentos());
	            }
	            if (atualizacao.getEmails() != null && !atualizacao.getEmails().isEmpty()) {
	                usuario.setEmails(atualizacao.getEmails());
	            }
	            if (atualizacao.getPerfis() != null && !atualizacao.getPerfis().isEmpty()) {
	                usuario.setPerfis(atualizacao.getPerfis());
	            }

	            repositorio.save(usuario);
	            status = HttpStatus.OK;
	        } else {
	            status = HttpStatus.NOT_FOUND;
	        }
	    }

	    return new ResponseEntity<>(status);
	}

	

	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirUsuario(@PathVariable long id) {
	    try {
	        Usuario usuario = repositorio.findById(id).orElse(null);
	        if (usuario == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
	        }

	        List<Empresa> empresas = repositorioEmpresa.findAll();
	        for (Empresa empresa : empresas) {
	            if (empresa.getUsuarios().contains(usuario)) {
	                empresa.getUsuarios().remove(usuario);
	                repositorioEmpresa.save(empresa);
	            }
	        }

	        List<Veiculo> veiculos = repositorioVeiculo.findAll();
	        for (Veiculo veiculo : veiculos) {
	            if (veiculo.getProprietario() != null && veiculo.getProprietario().equals(usuario)) {
	                veiculo.setProprietario(null);
	                repositorioVeiculo.save(veiculo);
	            }
	        }

	        List<Venda> vendas = repositorioVenda.findAll();
	        for (Venda venda : vendas) {
	            boolean alterado = false;
	            if (venda.getCliente() != null && venda.getCliente().equals(usuario)) {
	                venda.setCliente(null);
	                alterado = true;
	            }
	            if (venda.getFuncionario() != null && venda.getFuncionario().equals(usuario)) {
	                venda.setFuncionario(null);
	                alterado = true;
	            }
	            if (alterado) {
	                repositorioVenda.save(venda);
	            }
	        }

	        usuario.getMercadorias().clear();

	        usuario.getTelefones().clear();
	        usuario.getEmails().clear();
	        usuario.getDocumentos().clear();
	        usuario.getCredenciais().clear();

	        usuario.getVeiculos().clear();

	        usuario.getVendas().clear();

	        repositorio.delete(usuario);

	        return ResponseEntity.status(HttpStatus.OK).body("Usuário excluído com sucesso");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao excluir usuário");
	    }
	}



}