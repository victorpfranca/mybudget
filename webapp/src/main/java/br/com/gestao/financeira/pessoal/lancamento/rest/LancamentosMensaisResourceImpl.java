package br.com.gestao.financeira.pessoal.lancamento.rest;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import br.com.gestao.financeira.pessoal.LocalDateConverter;
import br.com.gestao.financeira.pessoal.conta.SaldoConta;
import br.com.gestao.financeira.pessoal.conta.SaldoDTO;
import br.com.gestao.financeira.pessoal.lancamento.LancamentosMensais;
import br.com.gestao.financeira.pessoal.lancamento.MeuFuturoResource;
import br.com.gestao.financeira.pessoal.view.AnoMes;

public class LancamentosMensaisResourceImpl implements MeuFuturoResource {

    @Inject
    private LancamentosMensais lancamentosMensais;

    @Override
    public List<SaldoDTO> lancamentos(Date start, Date end) {
        inicializarLancamentos(start, end);
        return saldosStream().map(this::converterDTO).sequential().sorted(Comparator.comparing(SaldoDTO::getDate))
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal lancamento(Integer ano, Integer mes, Date start, Date end) {
        inicializarLancamentos(start, end);
        return saldosStream().filter(saldo -> saldo.compareDate(ano, mes) == 0).map(SaldoConta::getValor).findFirst()
                .orElse(null);
    }

    @Override
    public SaldoDTO menor(Date start, Date end) {
        inicializarLancamentos(start, end);
        return saldosStream().map(this::converterDTO).min(Comparator.comparing(SaldoDTO::getValor)).orElse(null);
    }

    @Override
    public SaldoDTO maior(Date start, Date end) {
        return saldosStream().map(this::converterDTO).max(Comparator.comparing(SaldoDTO::getValor)).orElse(null);
    }

    @Override
    public SaldoDTO ultimo(Date start, Date end) {
        return saldosStream().map(this::converterDTO).max(Comparator.comparing(SaldoDTO::getDate)).orElse(null);
    }

    private SaldoDTO converterDTO(SaldoConta saldo) {
        return new SaldoDTO(saldoContaDateString(saldo), saldo.getValor());
    }

    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    private String saldoContaDateString(SaldoConta saldo) {
        return toString(LocalDate.of(saldo.getAno(), saldo.getMes(), 1));
    }

    private String toString(LocalDate date) {
        return df.format(LocalDateConverter.toDate(date));
    }

    private Stream<SaldoConta> saldosStream() {
        return lancamentosMensais.getSaldos().parallelStream();
    }

    private void inicializarLancamentos(Date start, Date end) {
        Function<Date, AnoMes> conversorDateAnoMes = Function.<Date> identity().andThen(LocalDateConverter::fromDate)
                .andThen(AnoMes::new);
        AnoMes anoMesInicial = Optional.ofNullable(start).map(conversorDateAnoMes).orElse(null);
        AnoMes anoMesFinal = Optional.ofNullable(end).map(conversorDateAnoMes).orElse(null);
        if (anoMesInicial != null || anoMesFinal != null)
            lancamentosMensais.inicializar(anoMesInicial, anoMesFinal);
    }

}
