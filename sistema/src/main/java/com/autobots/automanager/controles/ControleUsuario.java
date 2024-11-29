package com.autobots.automanager.controles;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.adicionadorLinks.AdicionadorLinkUsuario;
import com.autobots.automanager.configuracao.AuthenticationFacade;
import com.autobots.automanager.dto.UsuarioDto;
import com.autobots.automanager.entidades.CredencialUsuarioSenha;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelos.Perfil;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVeiculo;
import com.autobots.automanager.repositorios.RepositorioVenda;
import com.autobots.automanager.services.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class ControleUsuario {
	
	@Autowired
	private AuthenticationFacade authenticationFacade;

	@Autowired
	private UsuarioService usuarioService;
	
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
	
	@GetMapping("/listar")
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	public ResponseEntity<?> obterUsuarios() {
	    try {
	        // Obter o usuário autenticado
	        Usuario usuarioAutenticado = authenticationFacade.getUsuarioAutenticado();

	        // Buscar todos os usuários
	        List<Usuario> usuarios = usuarioService.listarTodos();

	        // Filtrar a lista com base nas permissões do usuário autenticado
	        List<Usuario> usuariosFiltrados = usuarios.stream().filter(usuario -> {
	            if (usuarioAutenticado.getPerfis().contains(Perfil.ROLE_ADMIN)) {
	                // Admin pode ver todos
	                return true;
	            } else if (usuarioAutenticado.getPerfis().contains(Perfil.ROLE_GERENTE)) {
	                // Gerente pode ver GERENTE, VENDEDOR e CLIENTE
	                return usuario.getPerfis().contains(Perfil.ROLE_GERENTE) ||
	                       usuario.getPerfis().contains(Perfil.ROLE_VENDEDOR) ||
	                       usuario.getPerfis().contains(Perfil.ROLE_CLIENTE);
	            } else if (usuarioAutenticado.getPerfis().contains(Perfil.ROLE_VENDEDOR)) {
	                // Vendedor pode ver VENDEDOR e CLIENTE
	                return usuario.getPerfis().contains(Perfil.ROLE_VENDEDOR) ||
	                       usuario.getPerfis().contains(Perfil.ROLE_CLIENTE);
	            }
	            return false; // Qualquer outro perfil não tem acesso
	        }).toList();

	        // Adicionar links aos usuários filtrados
	        adicionadorLink.adicionarLink(usuariosFiltrados);

	        return new ResponseEntity<>(usuariosFiltrados, HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>("Erro ao listar usuários: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

    	

	@GetMapping("/listar/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
	public ResponseEntity<?> obterUsuarioPorId(@PathVariable Long id) {
	    try {
	        // Obter usuário autenticado
	        Usuario usuarioAutenticado = authenticationFacade.getUsuarioAutenticado();

	        // Buscar usuário pelo ID
	        Optional<Usuario> usuarioOptional = usuarioService.buscarPorId(id);

	        if (usuarioOptional.isPresent()) {
	            Usuario usuario = usuarioOptional.get();

	            // Verificar permissões
	            if (usuarioAutenticado.getPerfis().contains(Perfil.ROLE_ADMIN)) {
	                // Admin pode visualizar todos
	                adicionadorLink.adicionarLink(usuario);
	                return new ResponseEntity<>(usuario, HttpStatus.OK);
	            } else if (usuarioAutenticado.getPerfis().contains(Perfil.ROLE_GERENTE)) {
	                // Gerente pode visualizar GERENTE, VENDEDOR e CLIENTE
	                if (usuario.getPerfis().contains(Perfil.ROLE_GERENTE) ||
	                    usuario.getPerfis().contains(Perfil.ROLE_VENDEDOR) ||
	                    usuario.getPerfis().contains(Perfil.ROLE_CLIENTE)) {
	                    adicionadorLink.adicionarLink(usuario);
	                    return new ResponseEntity<>(usuario, HttpStatus.OK);
	                }
	            } else if (usuarioAutenticado.getPerfis().contains(Perfil.ROLE_VENDEDOR)) {
	                // Vendedor pode visualizar VENDEDOR e CLIENTE
	                if (usuario.getPerfis().contains(Perfil.ROLE_VENDEDOR) ||
	                    usuario.getPerfis().contains(Perfil.ROLE_CLIENTE)) {
	                    adicionadorLink.adicionarLink(usuario);
	                    return new ResponseEntity<>(usuario, HttpStatus.OK);
	                }
	            } else if (usuarioAutenticado.getPerfis().contains(Perfil.ROLE_CLIENTE)) {
	                // Cliente pode visualizar apenas suas próprias informações
	                if (usuarioAutenticado.getId().equals(id)) {
	                    adicionadorLink.adicionarLink(usuario);
	                    return new ResponseEntity<>(usuario, HttpStatus.OK);
	                } else {
	                    return new ResponseEntity<>("Você só pode visualizar suas próprias informações.", HttpStatus.FORBIDDEN);
	                }
	            }

	            // Caso não tenha permissão
	            return new ResponseEntity<>("Acesso negado.", HttpStatus.FORBIDDEN);
	        } else {
	            return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.NOT_FOUND);
	        }
	    } catch (Exception e) {
	        return new ResponseEntity<>("Erro ao buscar o usuário: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}



    	
    	
    	@PostMapping("/cadastrar/{idempresa}")
    	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    	public ResponseEntity<?> cadastrarUsuario(
    	        @PathVariable Long idempresa, 
    	        @RequestBody UsuarioDto usuarioDto) {
    	    try {
    	    	
    	        Usuario usuarioAutenticado = authenticationFacade.getUsuarioAutenticado();

    	        Empresa empresa = repositorioEmpresa.findById(idempresa).orElse(null);

    	        if (empresa == null) {
    	            return ResponseEntity.badRequest().body("Empresa não encontrada.");
    	        }

    	        // Verificar as permissões com base nos perfis do usuário autenticado
    	        boolean permissoesValidas = usuarioDto.getPerfis().stream().allMatch(perfil -> {
    	            if (usuarioAutenticado.getPerfis().contains(Perfil.ROLE_ADMIN)) {
    	                return true; // Admin pode criar usuários com qualquer perfil
    	            } else if (usuarioAutenticado.getPerfis().contains(Perfil.ROLE_GERENTE)) {
    	                // Gerente pode criar usuários com perfis de GERENTE, VENDEDOR e CLIENTE
    	                return perfil == Perfil.ROLE_GERENTE || 
    	                       perfil == Perfil.ROLE_VENDEDOR || 
    	                       perfil == Perfil.ROLE_CLIENTE;
    	            } else if (usuarioAutenticado.getPerfis().contains(Perfil.ROLE_VENDEDOR)) {
    	                // Vendedor pode criar apenas usuários com perfil de CLIENTE
    	                return perfil == Perfil.ROLE_CLIENTE;
    	            }
    	            return false;
    	        });

    	        if (!permissoesValidas) {
    	            return ResponseEntity.status(HttpStatus.FORBIDDEN)
    	                    .body("Você não tem permissão para criar usuários com os perfis especificados.");
    	        }
    	        Usuario usuario = new Usuario();
    	        usuario.setNome(usuarioDto.getNome());
    	        usuario.setNomeSocial(usuarioDto.getNomeSocial());
    	        usuario.setPerfis(usuarioDto.getPerfis());
    	        usuario.setTelefones(usuarioDto.getTelefones());
    	        usuario.setEndereco(usuarioDto.getEndereco());
    	        usuario.setDocumentos(usuarioDto.getDocumentos());
    	        usuario.setEmails(usuarioDto.getEmails());

    	        if (usuarioDto.getNomeUsuario() != null && usuarioDto.getSenha() != null) {
    	            CredencialUsuarioSenha credencial = new CredencialUsuarioSenha();
    	            credencial.setNomeUsuario(usuarioDto.getNomeUsuario());

    	            BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();
    	            credencial.setSenha(codificador.encode(usuarioDto.getSenha()));
    	            credencial.setCriacao(new Date());

    	            usuario.setCredencial(credencial);
    	        } else {
    	            return ResponseEntity.badRequest().body("Nome de usuário e senha são obrigatórios.");
    	        }

    	        empresa.getUsuarios().add(usuario);

    	        repositorio.save(usuario);
    	        repositorioEmpresa.save(empresa);

    	        return ResponseEntity.ok("Usuário cadastrado com credencial e vinculado à empresa com sucesso.");
    	    } catch (Exception e) {
    	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    	                             .body("Erro ao cadastrar usuário: " + e.getMessage());
    	    }
    	}





    	
    	@PutMapping("/atualizar")
    	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
    	public ResponseEntity<?> atualizarUsuario(@RequestBody UsuarioDto atualizacaoDto) {
    	    try {
    	        if (atualizacaoDto.getId() == null) {
    	            return new ResponseEntity<>("ID do usuário é obrigatório para atualização.", HttpStatus.BAD_REQUEST);
    	        }
    	        Usuario usuarioAutenticado = authenticationFacade.getUsuarioAutenticado();

    	        Usuario usuario = repositorio.findById(atualizacaoDto.getId()).orElse(null);

    	        if (usuario == null) {
    	            return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.NOT_FOUND);
    	        }
    	        
    	        boolean permissoesValidas = false;
    	        if (usuarioAutenticado.getPerfis().contains(Perfil.ROLE_ADMIN)) {
    	            permissoesValidas = true;
    	        } else if (usuarioAutenticado.getPerfis().contains(Perfil.ROLE_GERENTE)) {
    	            permissoesValidas = usuario.getPerfis().stream().allMatch(perfil -> 
    	                perfil == Perfil.ROLE_GERENTE || 
    	                perfil == Perfil.ROLE_VENDEDOR || 
    	                perfil == Perfil.ROLE_CLIENTE
    	            );
    	        } else if (usuarioAutenticado.getPerfis().contains(Perfil.ROLE_VENDEDOR)) {
    	            permissoesValidas = usuario.getPerfis().stream().allMatch(perfil -> 
    	                perfil == Perfil.ROLE_CLIENTE
    	            );
    	        } else if (usuarioAutenticado.getPerfis().contains(Perfil.ROLE_CLIENTE)) {
    	            permissoesValidas = usuarioAutenticado.getId().equals(usuario.getId());
    	        }

    	        if (!permissoesValidas) {
    	            return new ResponseEntity<>("Você não tem permissão para atualizar este usuário.", HttpStatus.FORBIDDEN);
    	        }

    	        if (atualizacaoDto.getNome() != null) {
    	            usuario.setNome(atualizacaoDto.getNome());
    	        }
    	        if (atualizacaoDto.getNomeSocial() != null) {
    	            usuario.setNomeSocial(atualizacaoDto.getNomeSocial());
    	        }

    	        if (atualizacaoDto.getPerfis() != null && !atualizacaoDto.getPerfis().isEmpty()) {
    	            usuario.setPerfis(atualizacaoDto.getPerfis());
    	        }

    	        if (atualizacaoDto.getTelefones() != null) {
    	            usuario.getTelefones().clear();
    	            usuario.getTelefones().addAll(atualizacaoDto.getTelefones());
    	        }

    	        if (atualizacaoDto.getEmails() != null) {
    	            usuario.getEmails().clear();
    	            usuario.getEmails().addAll(atualizacaoDto.getEmails());
    	        }

    	        if (atualizacaoDto.getDocumentos() != null) {
    	            usuario.getDocumentos().clear();
    	            usuario.getDocumentos().addAll(atualizacaoDto.getDocumentos());
    	        }

    	        if (atualizacaoDto.getEndereco() != null) {
    	            usuario.setEndereco(atualizacaoDto.getEndereco());
    	        }

    	        if (atualizacaoDto.getNomeUsuario() != null || atualizacaoDto.getSenha() != null) {
    	            if (usuario.getCredencial() instanceof CredencialUsuarioSenha) {
    	                CredencialUsuarioSenha credencial = (CredencialUsuarioSenha) usuario.getCredencial();

    	                if (atualizacaoDto.getNomeUsuario() != null) {
    	                    credencial.setNomeUsuario(atualizacaoDto.getNomeUsuario());
    	                }
    	                if (atualizacaoDto.getSenha() != null) {
    	                    BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();
    	                    credencial.setSenha(codificador.encode(atualizacaoDto.getSenha()));
    	                }

    	                usuario.setCredencial(credencial);
    	            } else {
    	                return ResponseEntity.badRequest().body("Tipo de credencial inválido ou não suportado para atualização.");
    	            }
    	        }

    	        repositorio.save(usuario);

    	        return new ResponseEntity<>("Usuário atualizado com sucesso.", HttpStatus.OK);
    	    } catch (Exception e) {
    	        return new ResponseEntity<>("Erro ao atualizar usuário: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    	    }
    	}

    
    	@DeleteMapping("/excluir/{id}")
    	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
    	public ResponseEntity<?> excluirUsuario(@PathVariable long id) {
    	    try {
    	    	
    	    	Usuario usuarioAutenticado = authenticationFacade.getUsuarioAutenticado();
    	    	
    	        Usuario usuario = repositorio.findById(id).orElse(null);
    	        if (usuario == null) {
    	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
    	        }
    	        
    	        boolean permissoesValidas = false;
    	        if (usuarioAutenticado.getPerfis().contains(Perfil.ROLE_ADMIN)) {
    	            permissoesValidas = true; 
    	        } else if (usuarioAutenticado.getPerfis().contains(Perfil.ROLE_GERENTE)) {
    	            permissoesValidas = usuario.getPerfis().stream().allMatch(perfil -> 
    	                perfil == Perfil.ROLE_GERENTE || 
    	                perfil == Perfil.ROLE_VENDEDOR || 
    	                perfil == Perfil.ROLE_CLIENTE
    	            );
    	        } else if (usuarioAutenticado.getPerfis().contains(Perfil.ROLE_VENDEDOR)) {
    	            permissoesValidas = usuario.getPerfis().stream().allMatch(perfil -> 
    	                perfil == Perfil.ROLE_CLIENTE
    	            );
    	        }

    	        if (!permissoesValidas) {
    	            return new ResponseEntity<>("Você não tem permissão para excluir este usuário.", HttpStatus.FORBIDDEN);
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
    

    	        usuario.getVeiculos().clear();

    	        usuario.getVendas().clear();
    	        
    	        usuario.setCredencial(null);

    	        repositorio.delete(usuario);

    	        return ResponseEntity.status(HttpStatus.OK).body("Usuário excluído com sucesso");
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao excluir usuário");
    	    }
    	}

	
	}