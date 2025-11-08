package com.serv.oeste.domain.entities.service;

import com.serv.oeste.domain.entities.client.Client;
import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.enums.FormaPagamento;
import com.serv.oeste.domain.enums.HorarioPrevisto;
import com.serv.oeste.domain.enums.SituacaoServico;
import com.serv.oeste.domain.exceptions.ErrorCollector;
import com.serv.oeste.domain.exceptions.service.ServiceNotValidException;
import com.serv.oeste.domain.utils.StringUtils;

import java.time.LocalDate;

public class Service {
    private Integer id;
    private String equipamento;
    private String marca;
    private String filial;
    private String descricao;
    private SituacaoServico situacao;
    private HorarioPrevisto horarioPrevisto;
    private Double valor;
    private FormaPagamento formaPagamento;
    private Double valorPecas;
    private Double valorComissao;
    private LocalDate dataPagamentoComissao;
    private LocalDate dataAbertura;
    private LocalDate dataFechamento;
    private LocalDate dataInicioGarantia;
    private LocalDate dataFimGarantia;
    private LocalDate dataAtendimentoPrevisto;
    private LocalDate dataAtendimentoEfetiva;
    private Client cliente;
    private Technician tecnico;

    private Service(
            Integer id,
            String equipamento,
            String marca,
            String filial,
            String descricao,
            SituacaoServico situacao,
            HorarioPrevisto horarioPrevisto,
            Double valor,
            FormaPagamento formaPagamento,
            Double valorPecas,
            Double valorComissao,
            LocalDate dataPagamentoComissao,
            LocalDate dataAbertura,
            LocalDate dataFechamento,
            LocalDate dataInicioGarantia,
            LocalDate dataFimGarantia,
            LocalDate dataAtendimentoPrevisto,
            LocalDate dataAtendimentoEfetiva,
            Client cliente,
            Technician tecnico
    ) {
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

    private Service(
            String equipamento,
            String marca,
            String filial,
            String descricao,
            SituacaoServico situacao,
            HorarioPrevisto horarioPrevisto,
            LocalDate dataAtendimento,
            Client cliente,
            Technician tecnico
    ) {
        this.equipamento = equipamento;
        this.marca = marca;
        this.filial = filial;
        this.descricao = descricao;
        this.situacao = situacao;
        this.horarioPrevisto = horarioPrevisto;
        this.dataAtendimentoPrevisto = dataAtendimento;
        this.cliente = cliente;
        this.tecnico = tecnico;

        validate();

        this.descricao = getHistory("", situacao, descricao);
    }

    public static Service restore(
            Integer id,
            String equipamento,
            String marca,
            String filial,
            String descricao,
            SituacaoServico situacao,
            HorarioPrevisto horarioPrevisto,
            Double valor,
            FormaPagamento formaPagamento,
            Double valorPecas,
            Double valorComissao,
            LocalDate dataPagamentoComissao,
            LocalDate dataAbertura,
            LocalDate dataFechamento,
            LocalDate dataInicioGarantia,
            LocalDate dataFimGarantia,
            LocalDate dataAtendimentoPrevisto,
            LocalDate dataAtendimentoEfetiva,
            Client cliente,
            Technician tecnico
    ) {
        return new Service(
                id,
                equipamento,
                marca,
                filial,
                descricao,
                situacao,
                horarioPrevisto,
                valor,
                formaPagamento,
                valorPecas,
                valorComissao,
                dataPagamentoComissao,
                dataAbertura,
                dataFechamento,
                dataInicioGarantia,
                dataFimGarantia,
                dataAtendimentoPrevisto,
                dataAtendimentoEfetiva,
                cliente,
                tecnico
        );
    }

    public static Service create(
            String equipamento,
            String marca,
            String filial,
            String descricao,
            HorarioPrevisto horarioPrevisto,
            LocalDate dataAtendimento,
            Client cliente,
            Technician tecnico
    ) {
        return new Service(
                equipamento,
                marca,
                filial,
                descricao,
                inferSituacao(horarioPrevisto, dataAtendimento, tecnico),
                horarioPrevisto,
                dataAtendimento,
                cliente,
                tecnico
        );
    }

    public void update(
            String equipamento,
            String marca,
            String filial,
            String descricao,
            SituacaoServico situacao,
            HorarioPrevisto horarioPrevisto,
            Double valor,
            FormaPagamento formaPagamento,
            Double valorPecas,
            Double valorComissao,
            LocalDate dataPagamentoComissao,
            LocalDate dataFechamento,
            LocalDate dataInicioGarantia,
            LocalDate dataFimGarantia,
            LocalDate dataAtendimentoPrevisto,
            LocalDate dataAtendimentoEfetiva,
            Client cliente,
            Technician tecnico
    ) {
        this.equipamento = equipamento;
        this.marca = marca;
        this.filial = filial;
        this.descricao = getHistory(descricao, situacao, getDescricao());
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

        validate();
    }

    private static String getHistory(String history, SituacaoServico situacao, String descricao) {
        String formattedSituation = situacao.getSituacao().toUpperCase();

        if (history.isBlank() && isServiceOpening(situacao)) {
            formattedSituation = "ABERTURA: " + formattedSituation;
        }

        String newEntry = String.format(
                "[%TD] - %s - %s%n",
                LocalDate.now(),
                formattedSituation,
                descricao
        );

        if (!history.isBlank()) {
            return history + newEntry;
        }
        return newEntry;
    }

    private void validate() {
        ErrorCollector errors = new ErrorCollector();

        if (cliente == null)
            errors.add(ErrorFields.CLIENTE, "Cliente é obrigatório");
        if (tecnico == null && isTechnicianNeeded(situacao))
            errors.add(ErrorFields.TECNICO, "Técnico é obrigatório para atendimento");
        if (StringUtils.isBlank(equipamento))
            errors.add(ErrorFields.EQUIPAMENTO, "Equipamento é obrigatório");
        if (StringUtils.isBlank(marca))
            errors.add(ErrorFields.MARCA, "Marca é obrigatória");
        if (StringUtils.isBlank(descricao))
            errors.add(ErrorFields.DESCRICAO, "Descrição é obrigatória");
        if (descricao != null && descricao.length() < 10)
            errors.add(ErrorFields.DESCRICAO, "Descrição precisa ter pelo menos 10 caracteres");
        if (descricao != null && descricao.split(" ").length < 3)
            errors.add(ErrorFields.DESCRICAO, "Descrição precisa ter pelo menos 3 palavras");
        if (StringUtils.isBlank(filial))
            errors.add(ErrorFields.FILIAL, "A filial é obrigatória");
        if (valor != null && valor < 0)
            errors.add(ErrorFields.SERVICO, "O Valor não pode ser negativo.");
        if (valorComissao != null && valorComissao < 0)
            errors.add(ErrorFields.SERVICO, "O Valor da Comissão não pode ser negativo.");
        if (valorPecas != null && valorPecas < 0)
            errors.add(ErrorFields.SERVICO, "O Valor das Peças não pode ser negativo.");

        errors.throwIfAny(ServiceNotValidException::new);
    }

    private static SituacaoServico inferSituacao(HorarioPrevisto horarioPrevisto, LocalDate dataAtendimento, Technician tecnico) {
        if (horarioPrevisto == null || (dataAtendimento == null && tecnico == null))
            return SituacaoServico.AGUARDANDO_AGENDAMENTO;
        return SituacaoServico.AGUARDANDO_ATENDIMENTO;
    }

    private static boolean isTechnicianNeeded(SituacaoServico situacao) {
        return !situacao.equals(SituacaoServico.AGUARDANDO_AGENDAMENTO) && !situacao.equals(SituacaoServico.CANCELADO);
    }

    private static boolean isServiceOpening(SituacaoServico situacao) {
        return situacao.equals(SituacaoServico.AGUARDANDO_ATENDIMENTO) || situacao.equals(SituacaoServico.AGUARDANDO_AGENDAMENTO);
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

    public HorarioPrevisto getHorarioPrevisto() {
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

    public LocalDate getDataPagamentoComissao() {
        return dataPagamentoComissao;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public LocalDate getDataFechamento() {
        return dataFechamento;
    }

    public LocalDate getDataInicioGarantia() {
        return dataInicioGarantia;
    }

    public LocalDate getDataFimGarantia() {
        return dataFimGarantia;
    }

    public LocalDate getDataAtendimentoPrevisto() {
        return dataAtendimentoPrevisto;
    }

    public LocalDate getDataAtendimentoEfetiva() {
        return dataAtendimentoEfetiva;
    }

    public Client getCliente() {
        return cliente;
    }

    public Technician getTecnico() {
        return tecnico;
    }

    public Boolean getHasGarantia() {
        Boolean garatia = null;
        if (dataInicioGarantia != null) {
            LocalDate dataHoje = LocalDate.now();
            garatia = (dataInicioGarantia.isBefore(dataHoje) && dataFimGarantia.isAfter(dataHoje));
        }
        return garatia;
    }
}
