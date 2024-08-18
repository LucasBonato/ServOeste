package com.serv.oeste.models.servico;

import com.serv.oeste.models.cliente.Cliente;
import com.serv.oeste.models.enums.SituacaoServico;
import com.serv.oeste.models.tecnico.Tecnico;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Servico")
public class Servico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Equipamento", nullable = false)
    private String equipamento;

    @Column(name = "Marca", nullable = false)
    private String marca;

    @Column(name = "Filial", nullable = false)
    private String filial;

    @Column(name = "Descricao", nullable = false)
    private String descricao;

    @Column(name = "Situacao", nullable = false)
    private String situacao;

    @Column(name = "Horario_Previsto")
    private String horarioPrevisto;

    @Column(name = "Valor")
    private Double valor;

    @CurrentTimestamp
    @Column(name = "Data_Abertura", nullable = false)
    private Date dataAbertura;

    @Column(name = "Data_Fechamento")
    private Date dataFechamento;

    @Column(name = "Data_Inicio_Garantia")
    private Date dataInicioGarantia;

    @Column(name = "Data_Fim_Garantia")
    private Date dataFimGarantia;

    @Column(name = "Data_Atendimento_Previsto")
    private Date dataAtendimentoPrevisto;

    @Column(name = "Data_Atendimento_Efetiva")
    private Date dataAtendimentoEfetiva;

    @ManyToOne
    @JoinColumn(name = "Id_Cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "Id_Tecnico")
    private Tecnico tecnico;

    public Servico(String equipamento, String marca, String filial, String descricao, SituacaoServico situacao, String horarioPrevisto, Date dataAtendimento,Cliente cliente, Tecnico tecnico) {
        this.equipamento = equipamento;
        this.marca = marca;
        this.filial = filial;
        this.descricao = descricao;
        this.situacao = situacao.getSituacao();
        this.horarioPrevisto = horarioPrevisto;
        this.dataAtendimentoPrevisto = dataAtendimento;
        this.cliente = cliente;
        this.tecnico = tecnico;
    }
}