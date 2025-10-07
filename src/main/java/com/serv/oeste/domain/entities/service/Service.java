package com.serv.oeste.domain.entities.service;

import com.serv.oeste.domain.entities.client.Client;
import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.enums.FormaPagamento;
import com.serv.oeste.domain.enums.SituacaoServico;

import java.time.LocalDate;
import java.util.Date;

public class Service {
    private Integer id;
    private final String equipamento;
    private final String marca;
    private final String filial;
    private final String descricao;
    private final SituacaoServico situacao;
    private final String horarioPrevisto;
    private Double valor;
    private FormaPagamento formaPagamento;
    private Double valorPecas;
    private Double valorComissao;
    private Date dataPagamentoComissao;
    private Date dataAbertura;
    private Date dataFechamento;
    private Date dataInicioGarantia;
    private Date dataFimGarantia;
    private final Date dataAtendimentoPrevisto;
    private Date dataAtendimentoEfetiva;
    private final Client cliente;
    private final Technician tecnico;

    public Service(String equipamento, String marca, String filial, String descricao, SituacaoServico situacao, String horarioPrevisto, Date dataAtendimento, Client cliente, Technician tecnico) {
        this.equipamento = equipamento;
        this.marca = marca;
        this.filial = filial;
        this.descricao = getHistory("", situacao, descricao);
        this.situacao = situacao;
        this.horarioPrevisto = horarioPrevisto;
        this.dataAtendimentoPrevisto = dataAtendimento;
        this.cliente = cliente;
        this.tecnico = tecnico;
    }

    public Service(Integer id, String equipamento, String marca, String filial, String descricao, SituacaoServico situacao, String horarioPrevisto, Double valor, FormaPagamento formaPagamento, Double valorPecas, Double valorComissao, Date dataPagamentoComissao, Date dataAbertura, Date dataFechamento, Date dataInicioGarantia, Date dataFimGarantia, Date dataAtendimentoPrevisto, Date dataAtendimentoEfetiva, Client cliente, Technician tecnico) {
        this.id = id;
        this.equipamento = equipamento;
        this.marca = marca;
        this.filial = filial;
        this.descricao = descricao;
        this.situacao = situacao;
        this.horarioPrevisto = horarioPrevisto;
        this.valor = valor;
        this.formaPagamento = formaPagamento;
        this.valorPecas = valorPecas;
        this.valorComissao = valorComissao;
        this.dataPagamentoComissao = dataPagamentoComissao;
        this.dataAbertura = dataAbertura;
        this.dataFechamento = dataFechamento;
        this.dataInicioGarantia = dataInicioGarantia;
        this.dataFimGarantia = dataFimGarantia;
        this.dataAtendimentoPrevisto = dataAtendimentoPrevisto;
        this.dataAtendimentoEfetiva = dataAtendimentoEfetiva;
        this.cliente = cliente;
        this.tecnico = tecnico;
    }

    public Service(Integer id, String equipamento, String marca, String filial, String descricao, String pastDescription, SituacaoServico situacao, String horarioPrevisto, Double valor, FormaPagamento formaPagamento, Double valorPecas, Double valorComissao, Date dataPagamentoComissao, Date dataFechamento, Date dataInicioGarantia, Date dataFimGarantia, Date dataAtendimentoPrevisto, Date dataAtendimentoEfetiva, Client cliente, Technician tecnico) {
        this.id = id;
        this.equipamento = equipamento;
        this.marca = marca;
        this.filial = filial;
        this.descricao = getHistory(descricao, situacao, pastDescription);
        this.situacao = situacao;
        this.horarioPrevisto = horarioPrevisto;
        this.valor = valor;
        this.formaPagamento = formaPagamento;
        this.valorPecas = valorPecas;
        this.valorComissao = valorComissao;
        this.dataPagamentoComissao = dataPagamentoComissao;
        this.dataFechamento = dataFechamento;
        this.dataInicioGarantia = dataInicioGarantia;
        this.dataFimGarantia = dataFimGarantia;
        this.dataAtendimentoPrevisto = dataAtendimentoPrevisto;
        this.dataAtendimentoEfetiva = dataAtendimentoEfetiva;
        this.cliente = cliente;
        this.tecnico = tecnico;
    }

     private static String getHistory(String history, SituacaoServico situation, String description ) {
        String formattedSituation = situation.getSituacao().toUpperCase();

        if (history.isEmpty()) {
            formattedSituation = "ABERTURA: " + formattedSituation;
        }

        String newEntry = String.format(
                " %s - %s - [%TD]",
                formattedSituation,
                description,
                LocalDate.now()
        );

        if (!history.isEmpty()) {
            return history + newEntry;
        }

        return newEntry;
    }

    public Integer getId() {
        return id;
    }

    public String getEquipamento() {
        return equipamento;
    }

    public String getMarca() {
        return marca;
    }

    public String getFilial() {
        return filial;
    }

    public String getDescricao() {
        return descricao;
    }

    public SituacaoServico getSituacao() {
        return situacao;
    }

    public String getHorarioPrevisto() {
        return horarioPrevisto;
    }

    public Double getValor() {
        return valor;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public Double getValorPecas() {
        return valorPecas;
    }

    public Double getValorComissao() {
        return valorComissao;
    }

    public Date getDataPagamentoComissao() {
        return dataPagamentoComissao;
    }

    public Date getDataAbertura() {
        return dataAbertura;
    }

    public Date getDataFechamento() {
        return dataFechamento;
    }

    public Date getDataInicioGarantia() {
        return dataInicioGarantia;
    }

    public Date getDataFimGarantia() {
        return dataFimGarantia;
    }

    public Date getDataAtendimentoPrevisto() {
        return dataAtendimentoPrevisto;
    }

    public Date getDataAtendimentoEfetiva() {
        return dataAtendimentoEfetiva;
    }

    public Client getCliente() {
        return cliente;
    }

    public Technician getTecnico() {
        return tecnico;
    }
}
