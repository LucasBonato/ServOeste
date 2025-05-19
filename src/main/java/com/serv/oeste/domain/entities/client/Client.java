package com.serv.oeste.domain.entities.client;

public class Client {
    private Integer id;
    private String nome;
    private String telefoneFixo;
    private String telefoneCelular;
    private String endereco;
    private String bairro;
    private String municipio;

    public Client(Integer id, String nome, String telefoneFixo, String telefoneCelular, String endereco, String bairro, String municipio) {
        this.id = id;
        this.nome = nome;
        this.telefoneFixo = telefoneFixo;
        this.telefoneCelular = telefoneCelular;
        this.endereco = endereco;
        this.bairro = bairro;
        this.municipio = municipio;
    }

    public Client(String nome, String sobrenome, String telefoneFixo, String telefoneCelular, String endereco, String bairro, String municipio) {
        this.nome = nome + " " + sobrenome;
        this.telefoneFixo = telefoneFixo;
        this.telefoneCelular = telefoneCelular;
        this.endereco = endereco;
        this.bairro = bairro;
        this.municipio = municipio;
    }

    public void setAll(String nome, String sobrenome, String telefoneFixo, String telefoneCelular, String endereco, String bairro, String municipio) {
        this.nome = nome + " " + sobrenome;
        this.telefoneFixo = telefoneFixo;
        this.telefoneCelular = telefoneCelular;
        this.endereco = endereco;
        this.bairro = bairro;
        this.municipio = municipio;
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
