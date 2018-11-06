package br.com.gestao.financeira.pessoal.lancamento.extractors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.gestao.financeira.pessoal.categoria.Categoria;
import br.com.gestao.financeira.pessoal.conta.Conta;
import br.com.gestao.financeira.pessoal.controleacesso.CredentialsStore;
import br.com.gestao.financeira.pessoal.lancamento.Lancamento;
import br.com.gestao.financeira.pessoal.lancamento.LancamentoStatus;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class GeradorExtratoLancamentos {

	@EJB
	private CredentialsStore credentialsStore;

	@Inject
	private EntityManager em;

	public List<Lancamento> execute(int ano, int mes, Conta conta, Categoria categoria, LancamentoStatus status) {
		return execute(ano, mes, conta, categoria, BigDecimal.ZERO, status);
	}

	public List<Lancamento> execute(int ano, int mes, Conta conta, Categoria categoria, BigDecimal saldoInicial,
			LancamentoStatus status) {
		List<Lancamento> lancamentos = em
				.createNamedQuery(Lancamento.FIND_LANCAMENTO_CONTA_CORRENTE_QUERY, Lancamento.class)
				.setParameter("usuario", credentialsStore.recuperarIdUsuarioLogado()).setParameter("ano", ano)
				.setParameter("mes", mes).setParameter("conta", conta).setParameter("cartaoCreditoFatura", null)
				.setParameter("faturaCartao", null).setParameter("saldoInicial", null)
				.setParameter("categoria", categoria).setParameter("status", status).getResultList();

		List<Lancamento> extrato = new ArrayList<Lancamento>();
		BigDecimal saldoAnterior = saldoInicial;
		for (Iterator<Lancamento> iterator = lancamentos.iterator(); iterator.hasNext();) {
			Lancamento lancamento = (Lancamento) iterator.next();
			saldoAnterior = lancamento.somarSaldo(saldoAnterior);
			extrato.add(lancamento);
		}
		return extrato;
	}

}