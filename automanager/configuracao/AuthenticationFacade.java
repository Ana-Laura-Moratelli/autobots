package com.autobots.automanager.configuracao;

import com.autobots.automanager.entidades.CredencialUsuarioSenha;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.repositorios.RepositorioCredencialUsuarioSenha;
import com.autobots.automanager.repositorios.RepositorioUsuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {

    @Autowired
    private RepositorioCredencialUsuarioSenha repositorioCredencial;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    public Usuario getUsuarioAutenticado() {
        String nomeUsuario = SecurityContextHolder.getContext().getAuthentication().getName();

        CredencialUsuarioSenha credencial = repositorioCredencial.findByNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new RuntimeException("Credencial não encontrada"));

        return repositorioUsuario.findByCredencial(credencial)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para a credencial"));
    }
}
