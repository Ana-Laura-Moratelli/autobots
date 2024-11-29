package com.autobots.automanager.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.repositorios.RepositorioVenda;

@Service
public class VendaService {

	@Autowired
	private RepositorioVenda repositorioVenda;

	public List<Venda> buscarTodasVendas() {
		return repositorioVenda.findAll();
	}

	public antlr.collections.List buscarVendasPorVendedor(Long vendedorId) {
		return repositorioVenda.findByFuncionarioId(vendedorId);
	}

	public antlr.collections.List buscarVendasPorCliente(Long clienteId) {
		return repositorioVenda.findByClienteId(clienteId);
	}

	public Venda salvarVenda(Venda venda) {
		return repositorioVenda.save(venda);
	}

	public Optional<Venda> buscarVendaPorId(Long id) {
		return repositorioVenda.findById(id);
	}

	public void deletarVenda(Long id) {
		repositorioVenda.deleteById(id);
	}
}
