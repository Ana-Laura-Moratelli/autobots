package com.autobots.automanager.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class CredencialUsuarioSenha extends Credencial {

    @Column(nullable = false, unique = true)
    private String nomeUsuario;

    @Column(nullable = false)
    private String senha;

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

    // equals e hashCode
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;

        CredencialUsuarioSenha that = (CredencialUsuarioSenha) obj;

        if (nomeUsuario != null ? !nomeUsuario.equals(that.nomeUsuario) : that.nomeUsuario != null) return false;
        return senha != null ? senha.equals(that.senha) : that.senha == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (nomeUsuario != null ? nomeUsuario.hashCode() : 0);
        result = 31 * result + (senha != null ? senha.hashCode() : 0);
        return result;
    }

    // toString
    @Override
    public String toString() {
        return "CredencialUsuarioSenha{" +
                "nomeUsuario='" + nomeUsuario + '\'' +
                ", senha='" + senha + '\'' +
                "} " + super.toString();
    }
}
