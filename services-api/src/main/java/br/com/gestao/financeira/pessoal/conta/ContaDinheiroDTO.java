package br.com.gestao.financeira.pessoal.conta;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ContaDinheiroDTO extends ContaDTO{

	private BigDecimal saldoInicial;

}
