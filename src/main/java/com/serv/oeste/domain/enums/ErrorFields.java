package com.serv.oeste.domain.enums;

public enum ErrorFields {
    NOMESOBRENOME("nomeSobrenome"),
    TELEFONECELULAR("telefoneCelular"),
    TELEFONEFIXO("telefoneFixo"),
    TELEFONES("telefones"),
    CEP("cep"),
    ENDERECO("endereco"),
    MUNICIPIO("municipio"),
    BAIRRO("bairro"),
    CLIENTE("cliente"),
    TECNICO("tecnico"),
    EQUIPAMENTO("equipamento"),
    MARCA("marca"),
    DESCRICAO("descricao"),
    FILIAL("filial"),
    PERIODO("periodo"),
    DATA("data"),
    CONHECIMENTO("conhecimento"),
    SERVICO("servico"),
    USER("user"),
    AUTH("auth"),
    SITUACAO("situacao"),
    DATAFECHAMENTO("dataFechamento"),
    DATAPAGAMENTOCOMISSAO("dataPagamentoComissao"),
    DATAINICIOGARANTIA("dataInicioGarantia"),
    DATAFIMGARANTIA("dataFimGarantia"),
    DATAATENDIMENTOPREVISTO("dataAtendimentoPrevisto"),
    DATAATENDIMENTOEFETIVO("dataAtendimentoEfetivo"),
    VALOR("valor"),
    VALORPECAS("valorPecas"),
    VALORCOMISSAO("valorComissao"),
    FORMAPAGAMENTO("formaPagamento");

    private final String fieldName;

    ErrorFields(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return this.fieldName;
    }
}
