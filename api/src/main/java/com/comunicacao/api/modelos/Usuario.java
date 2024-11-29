package com.comunicacao.api.modelos;

import java.util.List;

import lombok.Data;

@Data
public class Usuario {
    private Long id;
    private String nome;
    private String nomeSocial;
    private List<String> perfis;
    private List<Telefone> telefones;
    private Endereco endereco;
    private List<Documento> documentos;
    private List<Email> emails;
    private List<Credencial> credenciais;
	public CredencialUsuarioSenha getCredencial() {
		// TODO Auto-generated method stub
		return null;
	}
    
}

// Classe Telefone
@Data
class Telefone {
    private Long id;
    private String ddd;
    private String numero;
}

// Classe Endereco
@Data
class Endereco {
    private Long id;
    private String estado;
    private String cidade;
    private String bairro;
    private String rua;
    private String numero;
    private String codigoPostal;
    private String informacoesAdicionais;
}

// Classe Documento
@Data
class Documento {
    private Long id;
    private String tipo;
    private String dataEmissao;
    private String numero;
}

// Classe Email
@Data
class Email {
    private Long id;
    private String endereco;
}

// Classe Credencial
@Data
class Credencial {
    private Long id;
    private String criacao;
    private String ultimoAcesso;
    private Boolean inativo;
    private String nomeUsuario;
    private String senha;
    private String dataCriacao;
}


