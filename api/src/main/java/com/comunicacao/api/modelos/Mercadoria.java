package com.comunicacao.api.modelos;

import lombok.Data;

@Data
public class Mercadoria {
    private Long id;
    private String validade;
    private String fabricacao;
    private String cadastro;
    private String nome;
    private Integer quantidade;
    private Double valor;
    private String descricao;
}
