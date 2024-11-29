package com.autobots.automanager.adaptadores;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.autobots.automanager.entidades.CredencialUsuarioSenha;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelos.Perfil;

@SuppressWarnings("serial")
public class UserDetailsImpl implements UserDetails {
	private Usuario usuario;

	public UserDetailsImpl(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> autoridades = new ArrayList<>();
		for (Perfil perfil : usuario.getPerfis()) {
			autoridades.add(new SimpleGrantedAuthority(perfil.name()));
		}
		return autoridades;
	}

	@Override
	public String getPassword() {
	    if (usuario.getCredencial() instanceof CredencialUsuarioSenha) {
	        return ((CredencialUsuarioSenha) usuario.getCredencial()).getSenha();
	    }
	    throw new IllegalStateException("Credencial não é do tipo CredencialUsuarioSenha");
	}

	@Override
	public String getUsername() {
	    if (usuario.getCredencial() instanceof CredencialUsuarioSenha) {
	        return ((CredencialUsuarioSenha) usuario.getCredencial()).getNomeUsuario();
	    }
	    throw new IllegalStateException("Credencial não é do tipo CredencialUsuarioSenha");
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}