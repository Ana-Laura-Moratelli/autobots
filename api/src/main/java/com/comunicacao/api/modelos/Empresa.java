package com.comunicacao.api.modelos;

import java.util.List;

import lombok.Data;

@Data
public class Empresa {
    private Long id;
    private String razaoSocial;
    private String nomeFantasia;
    private Endereco endereco;
    private String cadastro;
    private List<Telefone> telefones;
    private List<Usuario> usuarios;
    private List<Mercadoria> mercadorias;
    private List<Servico> servicos;
    private List<Venda> vendas;
}



