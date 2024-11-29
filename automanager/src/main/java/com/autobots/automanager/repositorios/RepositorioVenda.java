package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.entidades.Venda;

import antlr.collections.List;

public interface RepositorioVenda extends JpaRepository<Venda, Long> {
	// Metodo para buscar vendas por ID do vendedor (funcionário)
		List findByFuncionarioId(Long vendedorId);

		// Metodo para buscar vendas por ID do cliente
		List findByClienteId(Long clienteId);
}