package com.autobots.automanager.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.Usuario;


public interface RepositorioUsuario extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCredencial(Credencial credencial);

}