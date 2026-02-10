package com.serv.oeste.domain.entities.client;

import com.serv.oeste.domain.enums.ErrorFields;
import com.serv.oeste.domain.exceptions.ErrorCollector;
import com.serv.oeste.domain.exceptions.client.ClientNotValidException;
import com.serv.oeste.domain.utils.StringUtils;
import com.serv.oeste.domain.valueObjects.Phone;

public class Client {
    private Integer id;
    private String nome;
    private Phone telefoneFixo;
    private Phone telefoneCelular;
    private String endereco;
    private String bairro;
    private String municipio;

    private Client(
            Integer id,
            String nome,
            Phone telefoneFixo,
            Phone telefoneCelular,
            String endereco,
            String bairro,
            String municipio
    ) {
        this.id = id;
        this.nome = nome;
        this.telefoneFixo = telefoneFixo;
        this.telefoneCelular = telefoneCelular;
        this.endereco = endereco;
        this.bairro = bairro;
        this.municipio = municipio;
    }

    private Client(
            String nome,
            String sobrenome,
            Phone telefoneFixo,
            Phone telefoneCelular,
            String endereco,
            String bairro,
            String municipio
    ) {
        this.nome = (nome + " " + sobrenome).trim();
        this.telefoneFixo = telefoneFixo;
        this.telefoneCelular = telefoneCelular;
        this.endereco = endereco;
        this.bairro = bairro;
        this.municipio = municipio;

        validate(nome, sobrenome);
    }

    public static Client restore(
            Integer id,
            String nome,
            String telefoneFixo,
            String telefoneCelular,
            String endereco,
            String bairro,
            String municipio
    ) {
        Phone fixo = StringUtils.isNotBlank(telefoneFixo) ? Phone.of(telefoneFixo) : null;
        Phone celular = StringUtils.isNotBlank(telefoneCelular) ? Phone.of(telefoneCelular) : null;

        return new Client(
                id,
                nome,
                fixo,
                celular,
                endereco,
                bairro,
                municipio
        );
    }

    public static Client create(
            String nome,
            String sobrenome,
            String telefoneFixo,
            String telefoneCelular,
            String endereco,
            String bairro,
            String municipio
    ) {
        Phone fixo = StringUtils.isNotBlank(telefoneFixo) ? Phone.of(telefoneFixo) : null;
        Phone celular = StringUtils.isNotBlank(telefoneCelular) ? Phone.of(telefoneCelular) : null;

        return new Client(
                nome,
                sobrenome,
                fixo,
                celular,
                endereco,
                bairro,
                municipio
        );
    }

    public void update(
            String nome,
            String sobrenome,
            String telefoneFixo,
            String telefoneCelular,
            String endereco,
            String bairro,
            String municipio
    ) {
        this.nome = (nome + " " + sobrenome).trim();
        this.telefoneFixo = StringUtils.isNotBlank(telefoneFixo) ? Phone.of(telefoneFixo) : null;
        this.telefoneCelular = StringUtils.isNotBlank(telefoneCelular) ? Phone.of(telefoneCelular) : null;
        this.endereco = endereco;
        this.bairro = bairro;
        this.municipio = municipio;

        validate(nome, sobrenome);
    }

    private void validate(String nome, String sobrenome) {
        ErrorCollector errors = new ErrorCollector();

        if ((telefoneCelular == null || telefoneCelular.isPhoneBlank()) && (telefoneFixo == null || telefoneFixo.isPhoneBlank()))
            errors.add(ErrorFields.TELEFONES, "O cliente precisa ter no mínimo um telefone cadastrado!");
        if (!endereco.matches(".*\\d+.*"))
            errors.add(ErrorFields.ENDERECO, "É necessário possuir número no Endereço!");
        if (StringUtils.isBlank(endereco))
            errors.add(ErrorFields.ENDERECO, "O endereço é obrigatório");
        if (StringUtils.isBlank(bairro))
            errors.add(ErrorFields.BAIRRO, "O bairro é obrigatório");
        if (StringUtils.isBlank(municipio))
            errors.add(ErrorFields.MUNICIPIO, "O municipio é obrigatório");
        if (StringUtils.isBlank(nome))
            errors.add(ErrorFields.NOMESOBRENOME, "O nome é obrigatório");
        if (nome != null && nome.length() < 2)
            errors.add(ErrorFields.NOMESOBRENOME, "O nome precisa ter no minimo 2 caracteres");
        if (StringUtils.isBlank(sobrenome))
            errors.add(ErrorFields.NOMESOBRENOME, "O sobrenome é obrigatório");
        if (sobrenome != null && sobrenome.length() < 2)
            errors.add(ErrorFields.NOMESOBRENOME, "O sobrenome precisa ter no minimo 2 caracteres");

        errors.throwIfAny(ClientNotValidException::new);
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefoneFixo() {
        return telefoneFixo != null ? telefoneFixo.getPhone() : null;
    }

    public String getTelefoneCelular() {
        return telefoneCelular != null ? telefoneCelular.getPhone() : null;
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
