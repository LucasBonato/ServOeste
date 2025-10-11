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
    HORARIO("horario"),
    DATA("data"),
    CONHECIMENTO("conhecimento"),
    SERVICO("servico"),
    USER("user"),
    AUTH("auth"),
    SITUACAO("situacao");

    private final String fieldName;

    ErrorFields(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return this.fieldName;
    }
}
