package br.com.gestao.financeira.pessoal.lancamento;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizacaoSerieLancamentoDTO {
	@NotNull
	private Integer categoria;
	@NotNull
	private Integer conta;
	@NotNull
	private BigDecimal valor;
	@NotNull
	private Character status;
	private String comentario;
}