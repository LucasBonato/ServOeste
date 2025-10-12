package com.serv.oeste.infrastructure.entities.service;

import com.serv.oeste.domain.entities.service.Service;
import com.serv.oeste.domain.enums.FormaPagamento;
import com.serv.oeste.domain.enums.SituacaoServico;
import com.serv.oeste.infrastructure.entities.client.ClientEntity;
import com.serv.oeste.infrastructure.entities.technician.TechnicianEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;

import java.util.Date;

@Entity
@Table(name = "Servico")
@Data
@NoArgsConstructor
public class ServiceEntity {
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
    @Enumerated(EnumType.STRING)
    private SituacaoServico situacao;

    @Column(name = "Horario_Previsto")
    private String horarioPrevisto;

    @Column(name = "Valor")
    private Double valor;

    @Column(name = "Forma_Pagamento")
    @Enumerated(EnumType.STRING)
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
    private ClientEntity cliente;

    @ManyToOne
    @JoinColumn(name = "Id_Tecnico")
    private TechnicianEntity tecnico;

    public ServiceEntity(Service service) {
        this.id = service.getId();
        this.equipamento = service.getEquipamento();
        this.marca = service.getMarca();
        this.filial = service.getFilial();
        this.descricao = service.getDescricao();
        this.situacao = service.getSituacao();
        this.horarioPrevisto = service.getHorarioPrevisto();
        this.valor = service.getValor();
        this.formaPagamento = service.getFormaPagamento();
        this.valorPecas = service.getValorPecas();
        this.valorComissao = service.getValorComissao();
        this.dataPagamentoComissao = service.getDataPagamentoComissao();
        this.dataAbertura = service.getDataAbertura();
        this.dataFechamento = service.getDataFechamento();
        this.dataInicioGarantia = service.getDataInicioGarantia();
        this.dataFimGarantia = service.getDataFimGarantia();
        this.dataAtendimentoPrevisto = service.getDataAtendimentoPrevisto();
        this.dataAtendimentoEfetiva = service.getDataAtendimentoEfetiva();
        this.cliente = new ClientEntity(service.getCliente());
        this.tecnico = new TechnicianEntity(service.getTecnico());
    }

    public Service toService() {
        return new Service(
                this.id,
                this.equipamento,
                this.marca,
                this.filial,
                this.descricao,
                this.situacao,
                this.horarioPrevisto,
                this.valor,
                this.formaPagamento,
                this.valorPecas,
                this.valorComissao,
                this.dataPagamentoComissao,
                this.dataAbertura,
                this.dataFechamento,
                this.dataInicioGarantia,
                this.dataFimGarantia,
                this.dataAtendimentoPrevisto,
                this.dataAtendimentoEfetiva,
                this.cliente.toClient(),
                this.tecnico == null ? null : this.tecnico.toTechnician()
        );
    }
}
