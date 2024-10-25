package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelos.AdicionadorLinkEndereco;
import com.autobots.automanager.modelos.EnderecoAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;

@RestController
@RequestMapping("/endereco")
public class EnderecoControle {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private EnderecoRepositorio enderecoRepositorio;

    @Autowired
    private AdicionadorLinkEndereco adicionadorLink;

    @GetMapping("/{id}")
    public ResponseEntity<Endereco> obterEndereco(@PathVariable long id) {
        Endereco endereco = enderecoRepositorio.findById(id).orElse(null);
        if (endereco == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            adicionadorLink.adicionarLink(endereco);
            return new ResponseEntity<>(endereco, HttpStatus.FOUND);
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Endereco>> obterEndereco() {
        List<Endereco> enderecos = enderecoRepositorio.findAll();
        if (enderecos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            adicionadorLink.adicionarLink(enderecos);
            return new ResponseEntity<>(enderecos, HttpStatus.FOUND);
        }
    }

    @PostMapping("/cadastrar/{idCliente}")
    public ResponseEntity<?> cadastrarEndereco(@PathVariable Long idCliente, @RequestBody Endereco endereco) {
        Cliente cliente = clienteRepositorio.findById(idCliente).orElse(null);

        if (cliente == null) {
            return new ResponseEntity<>("Cliente não encontrado", HttpStatus.NOT_FOUND);
        }

        if (endereco.getId() == null) {
            cliente.setEndereco(endereco);
            clienteRepositorio.save(cliente);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarEndereco(@PathVariable Long id, @RequestBody Endereco atualizacao) {
        Endereco endereco = enderecoRepositorio.findById(id).orElse(null);

        if (endereco != null) {
            EnderecoAtualizador atualizador = new EnderecoAtualizador();
            atualizador.atualizar(endereco, atualizacao);
            enderecoRepositorio.save(endereco);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Endereço não encontrado com id: " + id, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> deletarEndereco(@PathVariable Long id) {
        Endereco endereco = enderecoRepositorio.findById(id).orElse(null);
        if (endereco != null) {
            List<Cliente> clientes = clienteRepositorio.findAll();
            for (Cliente cliente : clientes) {
                if (cliente.getEndereco() != null && cliente.getEndereco().getId().equals(id)) {
                    cliente.setEndereco(null);
                    clienteRepositorio.save(cliente);
                }
            }

            enderecoRepositorio.delete(endereco);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Endereço não encontrado com id: " + id, HttpStatus.NOT_FOUND);
        }
    }
}
