package br.com.gestao.financeira.pessoal.orcamento;

import static br.com.gestao.financeira.pessoal.orcamento.OrcadoRealMesCategoria.FIND_ALL;
import static br.com.gestao.financeira.pessoal.orcamento.OrcadoRealMesCategoria.FIND_BY_DESPESA_MONTH;
import static br.com.gestao.financeira.pessoal.orcamento.OrcadoRealMesCategoria.FIND_BY_RECEITA_MONTH;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.com.gestao.financeira.pessoal.InOut;
import br.com.gestao.financeira.pessoal.InOutConverter;
import br.com.gestao.financeira.pessoal.categoria.Categoria;
import br.com.gestao.financeira.pessoal.controleacesso.Usuario;

@Entity
@Table(name = "vw_orcado_real_categoria_mes")
@NamedQueries({
		@NamedQuery(name = FIND_ALL, query = "SELECT o FROM OrcadoRealMesCategoria o WHERE o.usuario.id = :usuario"),
		@NamedQuery(name = FIND_BY_DESPESA_MONTH, query = "SELECT o FROM OrcadoRealMesCategoria o where o.usuario.id = :usuario and ano = :ano and mes = :mes and inOut = '1' order by orcado-realizado asc"),
		@NamedQuery(name = FIND_BY_RECEITA_MONTH, query = "SELECT o FROM OrcadoRealMesCategoria o where o.usuario.id = :usuario and ano = :ano and mes = :mes and inOut = '0' order by orcado-realizado asc") })
public class OrcadoRealMesCategoria implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String FIND_ALL = "OrcadoRealMesCategoria.findAll";
	public static final String FIND_BY_RECEITA_MONTH = "OrcadoRealMesCategoria.findByReceitaMonth";
	public static final String FIND_BY_DESPESA_MONTH = "OrcadoRealMesCategoria.findByDespesaMonth";

	@Id
	@NotNull
	@Convert(converter = InOutConverter.class)
	@Column(name = "in_out", nullable = false, unique = false)
	private InOut inOut;

	@Id
	@NotNull
	@Column(name = "mes", nullable = false, unique = false)
	private Integer mes;

	@Id
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH }, optional = false)
	@JoinColumn(nullable = true, name = "categoria_id")
	private Categoria categoria;

	@NotNull
	@Column(name = "orcado", nullable = false, unique = false)
	private BigDecimal orcado;

	@NotNull
	@Column(name = "realizado", nullable = false, unique = false)
	private BigDecimal realizado;

	@NotNull
	@JoinColumn(name = "usuario_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Usuario usuario;

	public InOut getInOut() {
		return inOut;
	}

	public void setInOut(InOut inOut) {
		this.inOut = inOut;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public BigDecimal getOrcado() {
		return orcado != null ? orcado : BigDecimal.ZERO;
	}

	public void setOrcado(BigDecimal orcado) {
		this.orcado = orcado;
	}

	public BigDecimal getRealizado() {
		return realizado != null ? realizado : BigDecimal.ZERO;
	}

	public void setRealizado(BigDecimal realizado) {
		this.realizado = realizado;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public BigDecimal getSaldo() {
		return getOrcado().compareTo(getRealizado()) >= 0 ? getOrcado().subtract(getRealizado()) : BigDecimal.ZERO;
	}

	public BigDecimal getRealPercent() {
		BigDecimal realizado = BigDecimal.ZERO;
		if (getRealizado() != null)
			realizado = getRealizado();
		if (getOrcado() == null || getOrcado().compareTo(BigDecimal.ZERO) == 0)
			return BigDecimal.ZERO;

		return realizado.divide(getOrcado(), 2, RoundingMode.HALF_UP).multiply(BigDecimal.TEN.multiply(BigDecimal.TEN));
	}

}