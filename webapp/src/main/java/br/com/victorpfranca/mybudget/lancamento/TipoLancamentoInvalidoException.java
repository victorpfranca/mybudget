package br.com.victorpfranca.mybudget.lancamento;

import javax.ejb.ApplicationException;

import br.com.victorpfranca.mybudget.lancamento.rules.TipoContaException;

@ApplicationException(rollback = true)
public class TipoLancamentoInvalidoException extends TipoContaException {
	private static final long serialVersionUID = 1L;

	public TipoLancamentoInvalidoException(String message) {
		super(message);
	}

	public TipoLancamentoInvalidoException(String message, Throwable cause) {
		super(message, cause);
	}

}