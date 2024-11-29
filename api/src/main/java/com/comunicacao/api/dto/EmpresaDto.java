package com.comunicacao.api.dto;

import lombok.Data;

@Data
public class EmpresaDto {
    private Long id;
    private String razaoSocial;
    private String nomeFantasia;
    private String cadastro;
}
