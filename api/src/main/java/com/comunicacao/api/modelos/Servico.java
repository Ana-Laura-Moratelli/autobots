package com.comunicacao.api.modelos;

import lombok.Data;

@Data
public class Servico {
    private Long id;
    private String nome;
    private Double valor;
    private String descricao;
}
