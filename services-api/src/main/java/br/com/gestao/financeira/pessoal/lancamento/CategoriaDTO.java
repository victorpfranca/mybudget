package br.com.gestao.financeira.pessoal.lancamento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(of="id") @NoArgsConstructor @AllArgsConstructor @Data
public class CategoriaDTO {
    
    private Integer id;
    private String nome;
    
}