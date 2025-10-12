package com.serv.oeste.domain.entities.client;

import com.serv.oeste.domain.exceptions.ErrorCollector;
import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.client.ClientNotValidException;

public class Client {
    private Integer id;
    private String nome;
    private String telefoneFixo;
    private String telefoneCelular;
    private String endereco;
    private String bairro;
    private String municipio;

    public Client() { }

    public Client(Integer id, String nome, String telefoneFixo, String telefoneCelular, String endereco, String bairro, String municipio) {
        this.id = id;
        this.nome = nome;
        this.telefoneFixo = telefoneFixo;
        this.telefoneCelular = telefoneCelular;
        this.endereco = endereco;
        this.bairro = bairro;
        this.municipio = municipio;

        validate();
    }

    private Client(String fullName, String telefoneFixo, String telefoneCelular, String endereco, String bairro, String municipio) {
        this.nome = fullName;
        this.telefoneFixo = telefoneFixo;
        this.telefoneCelular = telefoneCelular;
        this.endereco = endereco;
        this.bairro = bairro;
        this.municipio = municipio;

        validate();
    }

    public static Client create(String nome, String sobrenome, String telefoneFixo, String telefoneCelular, String endereco, String bairro, String municipio) {
        String fullName = (nome + " " + sobrenome).trim();

        return new Client(
                fullName,
                telefoneFixo,
                telefoneCelular,
                endereco,
                bairro,
                municipio
        );
    }

    public void update(String nome, String sobrenome, String telefoneFixo, String telefoneCelular, String endereco, String bairro, String municipio) {
        this.nome = (nome + " " + sobrenome).trim();
        this.telefoneFixo = telefoneFixo;
        this.telefoneCelular = telefoneCelular;
        this.endereco = endereco;
        this.bairro = bairro;
        this.municipio = municipio;

        validate();
    }

    private void validate() {
        ErrorCollector errors = new ErrorCollector();

        if ((telefoneCelular == null || telefoneCelular.isBlank()) && (telefoneFixo == null || telefoneFixo.isBlank())) {
            errors.add(ErrorFields.TELEFONES, "O cliente precisa ter no mínimo um telefone cadastrado!");
        }

        if (!endereco.matches(".*\\d+.*")) {
            errors.add(ErrorFields.ENDERECO, "É necessário possuir número no Endereço!");
        }

        errors.throwIfAny(ClientNotValidException::new);
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefoneFixo() {
        return telefoneFixo;
    }

    public String getTelefoneCelular() {
        return telefoneCelular;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public String getMunicipio() {
        return municipio;
    }
}
