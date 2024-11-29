package com.autobots.automanager.controles;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

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

import com.autobots.automanager.adicionadorLinks.AdicionadorLinkVenda;
import com.autobots.automanager.configuracao.AuthenticationFacade;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelos.Perfil;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVeiculo;
import com.autobots.automanager.repositorios.RepositorioVenda;
import com.autobots.automanager.services.VendaService;

@RestController
@RequestMapping("/venda")
public class ControleVenda {
	@Autowired
	private RepositorioVenda repositorio;
	@Autowired
	private RepositorioEmpresa repositorioEmpresa;	
	
	@Autowired
	private RepositorioVeiculo repositorioVeiculo;	
	
	@Autowired
	private RepositorioUsuario repositorioUsuario;	
	
	@Autowired
	private AdicionadorLinkVenda adicionadorLink;
	
	@Autowired
	private AuthenticationFacade authenticationFacade;

	@Autowired
	private VendaService vendaService;

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
	public ResponseEntity<?> obterVenda(@PathVariable long id) {
	    Usuario usuarioAutenticado = authenticationFacade.getUsuarioAutenticado();

	    Venda venda = repositorio.findById(id).orElse(null);

	    if (venda == null) {
	        return new ResponseEntity<>("Venda não encontrada.", HttpStatus.NOT_FOUND);
	    }

	    boolean permissaoValida = false;

	    if (usuarioAutenticado.getPerfis().contains(Perfil.ROLE_ADMIN) ||
	        usuarioAutenticado.getPerfis().contains(Perfil.ROLE_GERENTE)) {
	        permissaoValida = true; 
	    } else if (usuarioAutenticado.getPerfis().contains(Perfil.ROLE_VENDEDOR)) {
	        permissaoValida = venda.getFuncionario().getId().equals(usuarioAutenticado.getId());
	    } else if (usuarioAutenticado.getPerfis().contains(Perfil.ROLE_CLIENTE)) {
	        permissaoValida = venda.getCliente().getId().equals(usuarioAutenticado.getId());
	    }

	    if (!permissaoValida) {
	        return new ResponseEntity<>("Você não tem permissão para visualizar esta venda.", HttpStatus.FORBIDDEN);
	    }

	    adicionadorLink.adicionarLink(venda);
	    return new ResponseEntity<>(venda, HttpStatus.OK);
	}

	@GetMapping("/listar")
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
	public ResponseEntity<?> obterVendas() {
	    Usuario usuarioAutenticado = authenticationFacade.getUsuarioAutenticado();
	    List<Venda> vendasFiltradas;

	    if (usuarioAutenticado.getPerfis().contains(Perfil.ROLE_ADMIN) ||
	        usuarioAutenticado.getPerfis().contains(Perfil.ROLE_GERENTE)) {
	        vendasFiltradas = repositorio.findAll(); 
	    } else if (usuarioAutenticado.getPerfis().contains(Perfil.ROLE_VENDEDOR)) {
	        vendasFiltradas = repositorio.findAll().stream()
	            .filter(venda -> venda.getFuncionario().getId().equals(usuarioAutenticado.getId()))
	            .toList(); 
	    } else if (usuarioAutenticado.getPerfis().contains(Perfil.ROLE_CLIENTE)) {
	        vendasFiltradas = repositorio.findAll().stream()
	            .filter(venda -> venda.getCliente().getId().equals(usuarioAutenticado.getId()))
	            .toList(); 
	    } else {
	        return new ResponseEntity<>("Você não tem permissão para visualizar vendas.", HttpStatus.FORBIDDEN);
	    }

	    if (vendasFiltradas.isEmpty()) {
	        return new ResponseEntity<>("Nenhuma venda encontrada.", HttpStatus.NOT_FOUND);
	    }

	    adicionadorLink.adicionarLink(vendasFiltradas);
	    return new ResponseEntity<>(vendasFiltradas, HttpStatus.OK);
	}


	@PostMapping("/cadastrar")
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	public ResponseEntity<?> cadastrarVenda(@RequestBody Venda venda) {
	    try {
	        Usuario usuarioAutenticado = authenticationFacade.getUsuarioAutenticado();

	        if (usuarioAutenticado.getPerfis().contains(Perfil.ROLE_VENDEDOR)) {
	            if (venda.getFuncionario() == null || !venda.getFuncionario().getId().equals(usuarioAutenticado.getId())) {
	                return new ResponseEntity<>("Vendedor só pode criar vendas realizadas por si mesmo.", HttpStatus.FORBIDDEN);
	            }
	        }

	        if (venda.getCliente() == null || venda.getCliente().getId() == null) {
	            return new ResponseEntity<>("Cliente inválido ou não informado.", HttpStatus.BAD_REQUEST);
	        }
	        Usuario cliente = repositorioUsuario.findById(venda.getCliente().getId())
	            .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
	        venda.setCliente(cliente);

	        if (venda.getFuncionario() == null || venda.getFuncionario().getId() == null) {
	            return new ResponseEntity<>("Funcionário inválido ou não informado.", HttpStatus.BAD_REQUEST);
	        }
	        Usuario funcionario = repositorioUsuario.findById(venda.getFuncionario().getId())
	            .orElseThrow(() -> new RuntimeException("Funcionário não encontrado."));
	        venda.setFuncionario(funcionario);

	        if (venda.getVeiculo() == null || venda.getVeiculo().getId() == null) {
	            return new ResponseEntity<>("Veículo inválido ou não informado.", HttpStatus.BAD_REQUEST);
	        }
	        Veiculo veiculo = repositorioVeiculo.findById(venda.getVeiculo().getId())
	            .orElseThrow(() -> new RuntimeException("Veículo não encontrado."));
	        venda.setVeiculo(veiculo);

	        venda.setCadastro(new Date());

	        vendaService.salvarVenda(venda);

	        return new ResponseEntity<>("Venda cadastrada com sucesso.", HttpStatus.CREATED);
	    } catch (Exception e) {
	        return new ResponseEntity<>("Erro ao cadastrar venda: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}



	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarVenda(@RequestBody Venda atualizacao) {
	    if (atualizacao.getId() == null) {
	        return new ResponseEntity<>("ID da venda não informado.", HttpStatus.BAD_REQUEST);
	    }

	    Venda venda = repositorio.findById(atualizacao.getId())
	        .orElseThrow(() -> new RuntimeException("Venda não encontrada."));

	    if (atualizacao.getIdentificacao() != null) {
	        venda.setIdentificacao(atualizacao.getIdentificacao());
	    }

	    if (atualizacao.getCliente() != null && atualizacao.getCliente().getId() != null) {
	        Usuario cliente = repositorioUsuario.findById(atualizacao.getCliente().getId())
	            .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
	        venda.setCliente(cliente);
	    }

	    if (atualizacao.getFuncionario() != null && atualizacao.getFuncionario().getId() != null) {
	        Usuario funcionario = repositorioUsuario.findById(atualizacao.getFuncionario().getId())
	            .orElseThrow(() -> new RuntimeException("Funcionário não encontrado."));
	        venda.setFuncionario(funcionario);
	    }

	    if (atualizacao.getVeiculo() != null && atualizacao.getVeiculo().getId() != null) {
	        Veiculo veiculo = repositorioVeiculo.findById(atualizacao.getVeiculo().getId())
	            .orElseThrow(() -> new RuntimeException("Veículo não encontrado."));
	        venda.setVeiculo(veiculo);
	    }

	    if (atualizacao.getCadastro() != null) {
	        venda.setCadastro(atualizacao.getCadastro());
	    }

	    repositorio.save(venda);

	    return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@DeleteMapping("/excluir/{id}")
	@Transactional
	public ResponseEntity<?> excluirVenda(@PathVariable long id) {
	    Venda venda = repositorio.findById(id).orElse(null);

	    if (venda == null) {
	        return new ResponseEntity<>("Venda não encontrada.", HttpStatus.NOT_FOUND);
	    }

	    if (venda.getCliente() != null) {
	        Usuario cliente = venda.getCliente();
	        cliente.getVendas().remove(venda);
	        repositorioUsuario.save(cliente);
	        venda.setCliente(null);
	    }

	    if (venda.getFuncionario() != null) {
	        Usuario funcionario = venda.getFuncionario();
	        funcionario.getVendas().remove(venda);
	        repositorioUsuario.save(funcionario);
	        venda.setFuncionario(null);
	    }

	    if (venda.getVeiculo() != null) {
	        Veiculo veiculo = venda.getVeiculo();
	        veiculo.getVendas().remove(venda);
	        repositorioVeiculo.save(veiculo);
	        venda.setVeiculo(null);
	    }

	    List<Empresa> empresas = repositorioEmpresa.findAll();
	    for (Empresa empresa : empresas) {
	        if (empresa.getVendas().contains(venda)) {
	            empresa.getVendas().remove(venda);
	            repositorioEmpresa.save(empresa);
	            break;
	        }
	    }

	    if (venda.getMercadorias() != null) {
	        venda.getMercadorias().clear();
	    }

	    if (venda.getServicos() != null) {
	        venda.getServicos().clear();
	    }

	    repositorio.saveAndFlush(venda);

	    repositorio.deleteById(id);

	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}





}