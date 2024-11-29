package com.autobots.automanager.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.autobots.automanager.modelos.Perfil;

@Entity
public class CredencialCodigoBarra extends Credencial {

    @Column(nullable = false, unique = true)
    private Long codigo;

    // Getter e Setter
    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public void add(Perfil perfil) {
        throw new UnsupportedOperationException("Operação não suportada para CredencialCodigoBarra.");
    }

   

    // equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CredencialCodigoBarra that = (CredencialCodigoBarra) o;
        return codigo != null && codigo.equals(that.codigo);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (codigo != null ? codigo.hashCode() : 0);
        return result;
    }
}
