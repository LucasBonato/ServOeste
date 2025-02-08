package com.serv.oeste.models.servico;

import com.serv.oeste.models.cliente.Cliente;
import com.serv.oeste.models.dtos.requests.ServicoUpdateRequest;
import com.serv.oeste.models.enums.FormaPagamento;
import com.serv.oeste.models.enums.SituacaoServico;
import com.serv.oeste.models.tecnico.Tecnico;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDate;
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
    private SituacaoServico situacao;

    @Column(name = "Horario_Previsto")
    private String horarioPrevisto;

    @Column(name = "Valor")
    private Double valor;

    @Column(name = "Forma_Pagamento")
    private FormaPagamento formaPagamento;

    @Column(name = "Valor_Pecas")
    private Double valorPecas;

    @Column(name = "Valor_Comissao")
    private Double valorComissao;

    @Column(name = "Data_Pagamento_Comissao")
    private Date dataPagamentoComissao;

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
        this.descricao = getHistory("", situacao, descricao);
        this.situacao = situacao;
        this.horarioPrevisto = horarioPrevisto;
        this.dataAtendimentoPrevisto = dataAtendimento;
        this.cliente = cliente;
        this.tecnico = tecnico;
    }

    public Servico(ServicoUpdateRequest servicoUpdateRequest, Servico servico, Cliente cliente, Tecnico tecnico) {
        this.id = servico.id;
        this.dataAbertura = servico.dataAbertura;
        this.cliente = cliente;
        this.tecnico = tecnico;
        this.equipamento = servicoUpdateRequest.equipamento();
        this.marca = servicoUpdateRequest.marca();
        this.filial = servicoUpdateRequest.filial();
        this.descricao = getHistory(servico.getDescricao(), servicoUpdateRequest.situacao(), servicoUpdateRequest.descricao());
        this.situacao = servicoUpdateRequest.situacao();
        this.horarioPrevisto = servicoUpdateRequest.horarioPrevisto();
        this.valor = servicoUpdateRequest.valor();
        this.dataFechamento = servicoUpdateRequest.dataFechamento();
        this.dataInicioGarantia = servicoUpdateRequest.dataInicioGarantia();
        this.dataFimGarantia = servicoUpdateRequest.dataFimGarantia();
        this.dataAtendimentoPrevisto = servicoUpdateRequest.dataAtendimentoPrevisto();
        this.dataAtendimentoEfetiva = servicoUpdateRequest.dataAtendimentoEfetiva();
    }

    private static String getHistory(String history, SituacaoServico situacao, String descricao) {
        return history + String.format("[%TD] - %s - %s%n", LocalDate.now(), situacao.getSituacao().toUpperCase(), descricao);
    }
}