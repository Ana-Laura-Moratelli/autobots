package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelos.AdicionadorLinkTelefone;
import com.autobots.automanager.modelos.TelefoneAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {

    @Autowired
    private TelefoneRepositorio telefoneRepositorio;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private AdicionadorLinkTelefone adicionadorLink;

    @GetMapping("/{id}")
    public ResponseEntity<Telefone> obterTelefone(@PathVariable long id) {
        Telefone telefone = telefoneRepositorio.findById(id).orElse(null);
        if (telefone == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            adicionadorLink.adicionarLink(telefone);
            return new ResponseEntity<>(telefone, HttpStatus.FOUND);
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Telefone>> obterTelefones() {
        List<Telefone> telefones = telefoneRepositorio.findAll();
        if (telefones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            adicionadorLink.adicionarLink(telefones);
            return new ResponseEntity<>(telefones, HttpStatus.FOUND);
        }
    }

    @PostMapping("/cadastrar/{idCliente}")
    public ResponseEntity<?> cadastrarTelefone(@PathVariable Long idCliente, @RequestBody Telefone telefone) {
        Cliente cliente = clienteRepositorio.findById(idCliente).orElse(null);

        if (cliente == null) {
            return new ResponseEntity<>("Cliente não encontrado", HttpStatus.NOT_FOUND);
        }

        if (telefone.getId() == null) {
            cliente.getTelefones().add(telefone);
            clienteRepositorio.save(cliente);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarTelefone(@PathVariable Long id, @RequestBody Telefone atualizacao) {
        Telefone telefone = telefoneRepositorio.findById(id).orElse(null);

        if (telefone != null) {
            TelefoneAtualizador atualizador = new TelefoneAtualizador();
            atualizador.atualizar(telefone, atualizacao);
            telefoneRepositorio.save(telefone);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Telefone não encontrado com id: " + id, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> deletarTelefone(@PathVariable Long id) {
        Telefone telefone = telefoneRepositorio.findById(id).orElse(null);
        if (telefone != null) {
            List<Cliente> clientes = clienteRepositorio.findAll();
            for (Cliente cliente : clientes) {
                cliente.getTelefones().removeIf(t -> t.getId().equals(id));
                clienteRepositorio.save(cliente);
            }

            telefoneRepositorio.delete(telefone);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Telefone não encontrado com id: " + id, HttpStatus.NOT_FOUND);
        }
    }
}
