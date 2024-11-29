package com.autobots.automanager.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.entidades.CredencialUsuarioSenha;

public interface RepositorioCredencialUsuarioSenha extends JpaRepository<CredencialUsuarioSenha, Long> {
    Optional<CredencialUsuarioSenha> findByNomeUsuario(String nomeUsuario);
}
