package com.serv.oeste.infrastructure.entities.client;

import com.serv.oeste.domain.entities.client.Client;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cliente")
@Data
@NoArgsConstructor
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "Telefone_Fixo", length = 10)
    private String telefoneFixo;

    @Column(name = "Telefone_Celular", length = 11)
    private String telefoneCelular;

    @Column(name = "Endereco")
    private String endereco;

    @Column(name = "Bairro")
    private String bairro;

    @Column(name = "Municipio")
    private String municipio;

    public ClientEntity(Client client) {
        this.id = client.getId();
        this.nome = client.getNome();
        this.telefoneFixo = client.getTelefoneFixo();
        this.telefoneCelular = client.getTelefoneCelular();
        this.endereco = client.getEndereco();
        this.bairro = client.getBairro();
        this.municipio = client.getMunicipio();
    }

    public Client toClient() {
        return Client.restore(
                this.id,
                this.nome,
                this.telefoneFixo,
                this.telefoneCelular,
                this.endereco,
                this.bairro,
                this.municipio
        );
    }
}
