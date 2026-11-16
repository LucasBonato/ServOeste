package com.serv.oeste.domain.entities.service;

import com.serv.oeste.domain.entities.client.Client;
import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.enums.*;
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
    private Periodo periodo;
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
            Periodo periodo,
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
        this.periodo = periodo;
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
            Periodo periodo,
            LocalDate dataAtendimento,
            Client cliente,
            Technician tecnico
    ) {
        this.equipamento = equipamento;
        this.marca = marca;
        this.filial = filial;
        this.descricao = descricao;
        this.situacao = situacao;
        this.periodo = periodo;
        this.dataAtendimentoPrevisto = dataAtendimento;
        this.cliente = cliente;
        this.tecnico = tecnico;

        validateCreation(situacao);

        this.descricao = getHistory("", situacao, descricao);
    }

    public static Service restore(
            Integer id,
            String equipamento,
            String marca,
            String filial,
            String descricao,
            SituacaoServico situacao,
            Periodo periodo,
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
                periodo,
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
            Periodo periodo,
            LocalDate dataAtendimento,
            Client cliente,
            Technician tecnico
    ) {
        return new Service(
                equipamento,
                marca,
                filial,
                descricao,
                inferSituacao(dataAtendimento),
                periodo,
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
            Periodo periodo,
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
        String descricaoAnterior = getDescricao();

        this.equipamento = equipamento;
        this.marca = marca;
        this.filial = filial;
        this.descricao = descricao;
        this.periodo = periodo;
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

        ErrorCollector errors = validateTransition(situacao);

        errors.throwIfAny(ServiceNotValidException::new);

        this.situacao = situacao;

        validateCommon(errors);

        this.descricao = getHistory(descricaoAnterior, situacao, descricao);
    }

    private static String getHistory(String history, SituacaoServico situacao, String descricao) {
        String formattedSituation = situacao.getSituacao().toUpperCase();

        if (history.isBlank() && situacao.isInicial()) {
            formattedSituation = "ABERTURA: " + formattedSituation;
        }

        String newEntry = String.format(
                "[%TD] - %s - %s",
                LocalDate.now(),
                formattedSituation,
                descricao != null ? descricao : ""
        );

        if (!history.isBlank()) {
            return history + newEntry;
        }
        return newEntry;
    }

    private void validateCreation(SituacaoServico situacao) {
        ErrorCollector errors = new ErrorCollector();

        if (situacao == null)
            errors.add(ErrorFields.SITUACAO, "Situação inicial é obrigatória");
        if (situacao != null && !situacao.isInicial())
            errors.add(ErrorFields.SITUACAO, "Situação inicial inválida: " + situacao);

        validateCommon(errors);
    }

    private ErrorCollector validateTransition(SituacaoServico destino) {
        ErrorCollector errors = new ErrorCollector();

        if (destino == null) {
            errors.add(ErrorFields.SITUACAO, "Situação de destino é obrigatória");
            return errors;
        }

        if (!situacao.podeAvancarPara(destino) && !situacao.podeRetornarPara(destino) && situacao != destino)
            errors.add(
                ErrorFields.SITUACAO,
                String.format(
                    "Transição inválida de situação: '%s' para '%s'",
                    situacao.getSituacao(),
                    destino.getSituacao()
                )
            );

        if (
            destino.equals(SituacaoServico.GARANTIA) &&
            (dataInicioGarantia == null || dataFimGarantia == null)
        )
            errors.add(
                ErrorFields.SITUACAO,
                String.format(
                    "Transição inválida de situação: '%s' para '%s' pois garantia precisa estar preenchida",
                    situacao.getSituacao(),
                    destino.getSituacao()
                )
            );

        return errors;
    }

    private void validateCommon(ErrorCollector errors) {
        if (cliente == null)
            errors.add(ErrorFields.CLIENTE, "Cliente é obrigatório");

        if (situacao.exigeTecnico() && tecnico == null)
            errors.add(ErrorFields.TECNICO, "Técnico é obrigatório para situação: " + situacao.getSituacao());

        if (situacao.exigeEquipamento() && StringUtils.isBlank(equipamento))
            errors.add(ErrorFields.EQUIPAMENTO, "Equipamento é obrigatório");

        if (situacao.exigeMarca() && StringUtils.isBlank(marca))
            errors.add(ErrorFields.MARCA, "Marca é obrigatória");

        if (situacao.exigeDescricao() && StringUtils.isBlank(descricao))
            errors.add(ErrorFields.DESCRICAO, "Descrição é obrigatória para a situação informada");
        if (situacao.exigeDescricao() && descricao != null && descricao.length() < 10)
            errors.add(ErrorFields.DESCRICAO, "Descrição precisa ter pelo menos 10 caracteres");
        if (situacao.exigeDescricao() && descricao != null && descricao.split(" ").length < 3)
            errors.add(ErrorFields.DESCRICAO, "Descrição precisa ter pelo menos 3 palavras");

        if (situacao.exigeFilial() && StringUtils.isBlank(filial))
            errors.add(ErrorFields.FILIAL, "A filial é obrigatória");

        if (situacao.exigePeriodo() && periodo == null)
            errors.add(ErrorFields.PERIODO, "O período é obrigatório");

        if (situacao.exigeValorServico() && valor == null)
            errors.add(ErrorFields.VALOR, "O Valor é obrigatório.");
        if (valor != null && valor < 0)
            errors.add(ErrorFields.VALOR, "O Valor não pode ser negativo.");

        if (situacao.exigeValorComissao() && valorComissao == null)
            errors.add(ErrorFields.VALORCOMISSAO, "O Valor da Comissão é obrigatório.");
        if (valorComissao != null && valorComissao < 0)
            errors.add(ErrorFields.VALORCOMISSAO, "O Valor da Comissão não pode ser negativo.");

        if (situacao.exigeValorPecas() && valorPecas == null)
            errors.add(ErrorFields.VALORPECAS, "O Valor das Peças é obrigatório.");
        if (valorPecas != null && valorPecas < 0)
            errors.add(ErrorFields.VALORPECAS, "O Valor das Peças não pode ser negativo.");

        if (situacao.exigeFormaPagamento() && formaPagamento == null)
            errors.add(ErrorFields.FORMAPAGAMENTO, "Forma de pagamento é obrigatória para a situação " + situacao.getSituacao());

        if (situacao.exigeDataFechamento() && dataFechamento == null)
            errors.add(ErrorFields.DATAFECHAMENTO, "Data fechamento é obrigatória para a situação " + situacao.getSituacao());

        if (situacao.exigeDataPagamentoComissao() && dataPagamentoComissao == null)
            errors.add(ErrorFields.DATAPAGAMENTOCOMISSAO, "Data Pagamento Comissao é obrigatória para a situação " + situacao.getSituacao());

        if (situacao.exigeDataInicioGarantia() && dataInicioGarantia == null)
            errors.add(ErrorFields.DATAINICIOGARANTIA, "Data Inicio Garantia é obrigatória para a situação " + situacao.getSituacao());

        if (situacao.exigeDataFimGarantia() && dataFimGarantia == null)
            errors.add(ErrorFields.DATAFIMGARANTIA, "Data Fim Garantia é obrigatória para a situação " + situacao.getSituacao());

        if (situacao.exigeDataAtendimentoPrevisto() && dataAtendimentoPrevisto == null)
            errors.add(ErrorFields.DATAATENDIMENTOPREVISTO, "Data Atendimento Previsto é obrigatória para a situação " + situacao.getSituacao());

        if (situacao.exigeDataAtendimentoEfetivo() && dataAtendimentoEfetiva == null)
            errors.add(ErrorFields.DATAATENDIMENTOEFETIVO, "Data Atendimento Efetivo é obrigatória para a situação " + situacao.getSituacao());

        errors.throwIfAny(ServiceNotValidException::new);
    }

    private static SituacaoServico inferSituacao(LocalDate dataAtendimento) {
        return dataAtendimento == null
            ? SituacaoServico.AGUARDANDO_AGENDAMENTO
            : SituacaoServico.AGUARDANDO_ATENDIMENTO;
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

    public Periodo getPeriodo() {
        return periodo;
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
            garatia = dataFimGarantia != null
                    ? (dataInicioGarantia.isBefore(dataHoje) && dataFimGarantia.isAfter(dataHoje))
                    : (dataInicioGarantia.isBefore(dataHoje) && dataInicioGarantia.plusMonths(3).isAfter(dataHoje));
        }
        return garatia;
    }
}