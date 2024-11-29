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

import com.autobots.automanager.adicionadorLinks.AdicionadorLinkCredencialUsuarioSenha;
import com.autobots.automanager.entidades.CredencialUsuarioSenha;
import com.autobots.automanager.repositorios.RepositorioCredencialUsuarioSenha;

@RestController
@RequestMapping("/credencialusuariosenha")
public class ControleCredencialUsuarioSenha {
    @Autowired
    private RepositorioCredencialUsuarioSenha repositorio;
       
    @Autowired
    private AdicionadorLinkCredencialUsuarioSenha adicionadorLink;

    @GetMapping("/{id}")
    public ResponseEntity<CredencialUsuarioSenha> obterCredencialUsuarioSenha(@PathVariable long id) {
        List<CredencialUsuarioSenha> credenciais = repositorio.findAll();
        CredencialUsuarioSenha credencial = null;

        for (CredencialUsuarioSenha c : credenciais) {
            if (c.getId() == id) {
                credencial = c;
                break;
            }
        }

        if (credencial == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        adicionadorLink.adicionarLink(credencial);
        return new ResponseEntity<>(credencial, HttpStatus.OK);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<CredencialUsuarioSenha>> obterCredenciaisUsuarioSenha() {
        List<CredencialUsuarioSenha> credenciais = repositorio.findAll();

        if (credenciais.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        adicionadorLink.adicionarLink(credenciais);
        return new ResponseEntity<>(credenciais, HttpStatus.OK);
    }



	@PostMapping("/cadastrar")
	public ResponseEntity<?> cadastrarCredencialUsuarioSenha(@RequestBody CredencialUsuarioSenha credencial) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (credencial.getId() == null) {
			repositorio.save(credencial);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}

	@PutMapping("/atualizar/{id}")
	public ResponseEntity<?> atualizarCredencialUsuarioSenha(@PathVariable long id, @RequestBody CredencialUsuarioSenha atualizacao) {
	    try {
	        CredencialUsuarioSenha credencial = repositorio.findById(id).orElse(null);

	        if (credencial == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credencial não encontrada");
	        }

	        if (atualizacao.getNomeUsuario() != null) {
	            credencial.setNomeUsuario(atualizacao.getNomeUsuario());
	        }
	        if (atualizacao.getSenha() != null) {
	            credencial.setSenha(atualizacao.getSenha());
	        }

	        repositorio.save(credencial);

	        return ResponseEntity.status(HttpStatus.OK).body("Credencial atualizada com sucesso");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar credencial");
	    }
	}

	
	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirCredencialUsuarioSenha(@PathVariable long id) {
	    try {
	        CredencialUsuarioSenha credencial = repositorio.findById(id).orElse(null);

	        if (credencial == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credencial não encontrada");
	        }

	        repositorio.delete(credencial);

	        return ResponseEntity.status(HttpStatus.OK).body("Credencial excluída com sucesso");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao excluir credencial");
	    }
	}






}