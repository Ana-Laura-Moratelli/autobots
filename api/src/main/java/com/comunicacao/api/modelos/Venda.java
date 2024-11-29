package com.comunicacao.api.modelos;

import java.util.List;
import lombok.Data;

@Data
public class Venda {
    private Long id;
    private String cadastro;
    private String identificacao;
    private Usuario cliente;
    private Usuario funcionario;
    private List<Mercadoria> mercadorias;
    private List<Servico> servicos;
    private Veiculo veiculo;
}
