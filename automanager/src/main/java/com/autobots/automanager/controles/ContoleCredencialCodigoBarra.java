package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.adicionadorLinks.AdicionadorLinkCredencialCodigoBarra;
import com.autobots.automanager.entidades.CredencialCodigoBarra;
import com.autobots.automanager.repositorios.RepositorioCredencialCodigoBarra;

@RestController
@RequestMapping("/credencialcodigobarra")
public class ContoleCredencialCodigoBarra {

    @Autowired
    private RepositorioCredencialCodigoBarra repositorio;

    @Autowired
    private AdicionadorLinkCredencialCodigoBarra adicionadorLink;

    @GetMapping("/{id}")
    public ResponseEntity<CredencialCodigoBarra> obterCredencialCodigoBarra(@PathVariable long id) {
        CredencialCodigoBarra credencial = repositorio.findById(id).orElse(null);

        if (credencial == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        adicionadorLink.adicionarLink(credencial);
        return new ResponseEntity<>(credencial, HttpStatus.OK);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<CredencialCodigoBarra>> obterCredenciaisCodigoBarra() {
        List<CredencialCodigoBarra> credenciais = repositorio.findAll();
        if (credenciais.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        adicionadorLink.adicionarLink(credenciais);
        return new ResponseEntity<>(credenciais, HttpStatus.OK);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarCredencialCodigoBarra(@RequestBody CredencialCodigoBarra credencial) {
        try {
            repositorio.save(credencial);
            return new ResponseEntity<>("Credencial cadastrada com sucesso", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erro ao cadastrar credencial", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarCredencialCodigoBarra(
            @PathVariable long id,
            @RequestBody CredencialCodigoBarra credencialAtualizada) {
        try {
            CredencialCodigoBarra credencial = repositorio.findById(id).orElse(null);
            if (credencial == null) {
                return new ResponseEntity<>("Credencial não encontrada", HttpStatus.NOT_FOUND);
            }

            if (credencialAtualizada.getCodigo() != 0) {
                credencial.setCodigo(credencialAtualizada.getCodigo());
            }

            repositorio.save(credencial);

            return new ResponseEntity<>("Credencial atualizada com sucesso", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erro ao atualizar credencial", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> excluirCredencialCodigoBarra(@PathVariable long id) {
        try {
            CredencialCodigoBarra credencial = repositorio.findById(id).orElse(null);
            if (credencial == null) {
                return new ResponseEntity<>("Credencial não encontrada", HttpStatus.NOT_FOUND);
            }

            repositorio.delete(credencial);

            return new ResponseEntity<>("Credencial excluída com sucesso", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erro ao excluir credencial", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
