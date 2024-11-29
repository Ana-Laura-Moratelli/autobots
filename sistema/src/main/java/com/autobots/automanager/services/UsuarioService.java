package com.autobots.automanager.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.configuracao.AuthenticationFacade;
import com.autobots.automanager.modelos.Perfil;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@Service
public class UsuarioService {

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    public List<Usuario> listarTodos() {
        return repositorioUsuario.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return repositorioUsuario.findById(id);
    }

    public Usuario salvarUsuario(Usuario usuario) {
        return repositorioUsuario.save(usuario);
    }

    public void excluirUsuario(Long id) {
        repositorioUsuario.deleteById(id);
    }

    public boolean temPermissaoParaAlterar(Usuario usuarioAlvo) {
        Usuario usuarioAutenticado = authenticationFacade.getUsuarioAutenticado();

        if (usuarioAutenticado.getPerfis().contains(Perfil.ROLE_ADMIN)) {
            return true;
        }

        if (usuarioAutenticado.getPerfis().contains(Perfil.ROLE_GERENTE)) {
            return usuarioAlvo.getPerfis().stream().allMatch(perfil ->
                    perfil == Perfil.ROLE_VENDEDOR || perfil == Perfil.ROLE_CLIENTE);
        }

        if (usuarioAutenticado.getPerfis().contains(Perfil.ROLE_VENDEDOR)) {
            return usuarioAlvo.getPerfis().contains(Perfil.ROLE_CLIENTE);
        }

        return usuarioAlvo.getId().equals(usuarioAutenticado.getId());
    }

   

    public Usuario getUsuarioAutenticado() {
        return authenticationFacade.getUsuarioAutenticado();
    }
}