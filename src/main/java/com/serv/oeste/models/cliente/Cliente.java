package com.serv.oeste.models.cliente;

import com.serv.oeste.models.dtos.requests.ClienteRequest;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "Telefone_Fixo", length = 11)
    private String telefoneFixo;

    @Column(name = "Telefone_Celular", length = 11)
    private String telefoneCelular;

    @Column(name = "Endereco")
    private String endereco;

    @Column(name = "Bairro")
    private String bairro;

    @Column(name = "Municipio")
    private String municipio;

    public Cliente(ClienteRequest clienteRequest) {
        this.nome = clienteRequest.nome() + " " + clienteRequest.sobrenome();
        this.telefoneFixo = clienteRequest.telefoneFixo();
        this.telefoneCelular = clienteRequest.telefoneCelular();
        this.endereco = clienteRequest.endereco();
        this.bairro = clienteRequest.bairro();
        this.municipio = clienteRequest.municipio();
    }

    public void setAll(ClienteRequest clienteRequest) {
        this.nome = clienteRequest.nome() + " " + clienteRequest.sobrenome();
        this.telefoneFixo = clienteRequest.telefoneFixo();
        this.telefoneCelular = clienteRequest.telefoneCelular();
        this.endereco = clienteRequest.endereco();
        this.bairro = clienteRequest.bairro();
        this.municipio = clienteRequest.municipio();
    }
}
