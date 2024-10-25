package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelos.AdicionadorLinkDocumento;
import com.autobots.automanager.modelos.DocumentoAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {

    @Autowired
    private DocumentoRepositorio documentoRepositorio;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private AdicionadorLinkDocumento adicionadorLink;

    @GetMapping("/{id}")
    public ResponseEntity<Documento> obterDocumento(@PathVariable long id) {
        Documento documento = documentoRepositorio.findById(id).orElse(null);
        if (documento == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            adicionadorLink.adicionarLink(documento);
            return new ResponseEntity<>(documento, HttpStatus.FOUND);
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Documento>> obterDocumentos() {
        List<Documento> documentos = documentoRepositorio.findAll();
        if (documentos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            adicionadorLink.adicionarLink(documentos);
            return new ResponseEntity<>(documentos, HttpStatus.FOUND);
        }
    }

    @PostMapping("/cadastrar/{idCliente}")
    public ResponseEntity<?> cadastrarDocumento(@PathVariable Long idCliente, @RequestBody Documento documento) {
        Cliente cliente = clienteRepositorio.findById(idCliente).orElse(null);

        if (cliente == null) {
            return new ResponseEntity<>("Cliente não encontrado", HttpStatus.NOT_FOUND);
        }

        if (documento.getId() == null) {
            cliente.getDocumentos().add(documento);
            clienteRepositorio.save(cliente);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarDocumento(@PathVariable Long id, @RequestBody Documento atualizacao) {
        Documento documento = documentoRepositorio.findById(id).orElse(null);

        if (documento != null) {
            DocumentoAtualizador atualizador = new DocumentoAtualizador();
            atualizador.atualizar(documento, atualizacao);
            documentoRepositorio.save(documento);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Documento não encontrado com id: " + id, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> deletarDocumento(@PathVariable Long id) {
        Documento documento = documentoRepositorio.findById(id).orElse(null);
        if (documento != null) {
            List<Cliente> clientes = clienteRepositorio.findAll();
            for (Cliente cliente : clientes) {
                cliente.getDocumentos().removeIf(d -> d.getId().equals(id));
                clienteRepositorio.save(cliente);
            }

            documentoRepositorio.delete(documento);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Documento não encontrado com id: " + id, HttpStatus.NOT_FOUND);
        }
    }
}
