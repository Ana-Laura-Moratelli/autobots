package com.autobots.automanager.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;

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

    // equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CredencialCodigoBarra that = (CredencialCodigoBarra) o;
        return codigo == that.codigo;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Long.hashCode(codigo);
        return result;
    }

	public Object getUsuario() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setUsuario(Object usuario) {
		// TODO Auto-generated method stub
		
	}
}
