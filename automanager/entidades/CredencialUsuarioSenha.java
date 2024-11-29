package com.autobots.automanager.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.autobots.automanager.modelos.Perfil;

@Entity
public class CredencialUsuarioSenha extends Credencial {

    @Column(nullable = false, unique = true)
    private String nomeUsuario;

    @Column(nullable = false)
    private String senha;

    public void add(Perfil perfil) {
        System.out.println("Perfil adicionado: " + perfil.name());
    }

    // Getters e Setters
    
    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return "CredencialUsuarioSenha{" +
                "nomeUsuario='" + nomeUsuario + '\'' +
                ", senha='" + senha + '\'' +
                "} " + super.toString();
    }
}
