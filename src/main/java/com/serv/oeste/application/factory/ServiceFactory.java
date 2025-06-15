package com.serv.oeste.application.factory;

import com.serv.oeste.application.dtos.reponses.ServicoResponse;
import com.serv.oeste.application.dtos.requests.ServicoRequest;
import com.serv.oeste.application.dtos.requests.ServicoUpdateRequest;
import com.serv.oeste.domain.entities.client.Client;
import com.serv.oeste.domain.entities.service.Service;
import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.enums.FormaPagamento;
import com.serv.oeste.domain.enums.SituacaoServico;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServiceFactory {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public static Service create(
            Integer id,
            String equipamento,
            String marca,
            String filial,
            String descricao,
            SituacaoServico situacao,
            String horarioPrevisto,
            Double valor,
            FormaPagamento formaPagamento,
            Double valorPecas,
            Double valorComissao,
            Date dataPagamentoComissao,
            Date dataAbertura,
            Date dataFechamento,
            Date dataInicioGarantia,
            Date dataFimGarantia,
            Date dataAtendimentoPrevisto,
            Date dataAtendimentoEfetiva,
            Client cliente,
            Technician tecnico
    ) {
        return new Service(
                id,
                equipamento,
                marca,
                filial,
                descricao,
                situacao,
                horarioPrevisto,
                valor,
                formaPagamento,
                valorPecas,
                valorComissao,
                dataPagamentoComissao,
                dataAbertura,
                dataFechamento,
                dataInicioGarantia,
                dataFimGarantia,
                dataAtendimentoPrevisto,
                dataAtendimentoEfetiva,
                cliente,
                tecnico
        );
    }



    public static Service createMinimal(
            String equipamento,
            String marca,
            String filial,
            String descricao,
            SituacaoServico situacao,
            String horarioPrevisto,
            Date dataAtendimento,
            Client cliente,
            Technician tecnico
    ) {
        return new Service(
                equipamento,
                marca,
                filial,
                descricao,
                situacao,
                horarioPrevisto,
                dataAtendimento,
                cliente,
                tecnico
        );
    }

    public static Service createWithGarantia(Date dataInicioGarantia, Date dataFimGarantia) {
        return create(
                99,
                "Monitor",
                "Samsung",
                "Osasco",
                "Troca de tela",
                SituacaoServico.AGUARDANDO_ATENDIMENTO,
                "TARDE",
                250.0,
                FormaPagamento.PIX,
                100.0,
                25.0,
                new Date(),
                new Date(),
                new Date(),
                dataInicioGarantia,
                dataFimGarantia,
                new Date(),
                new Date(),
                ClientFactory.createDefault(),
                TechnicianFactory.createDefault()
        );
    }

    public static ServicoRequest createValidServiceRequest(int clientId, int technicianId) {
        return new ServicoRequest(
                clientId,
                technicianId,
                "Lava Roupa",
                "Brastemp",
                "Carapicuíba",
                null,
                null,
                "Maquina com mal funcionamento"
        );
    }

    // Service creation methods
    public static Service createDefault() {
        return create(
                1,
                "Notebook",
                "Dell",
                "São Paulo",
                "Problema com teclado",
                SituacaoServico.AGUARDANDO_ATENDIMENTO,
                "TARDE",
                500.0,
                FormaPagamento.BOLETO,
                100.0,
                50.0,
                parseDate("15/07/2023"),
                parseDate("01/07/2023"),
                parseDate("10/07/2023"),
                parseDate("11/07/2023"),
                parseDate("11/08/2023"),
                parseDate("05/07/2023"),
                parseDate("08/07/2023"),
                ClientFactory.createDefault(),
                TechnicianFactory.createDefault()
        );
    }

    public static Service createServiceWithActiveWarranty() {
        return createWithGarantia(
                parseDate("01/01/2023"),
                parseDate("01/01/2024") // Future date for active warranty
        );
    }

    public static Service createServiceWithExpiredWarranty() {
        return createWithGarantia(
                parseDate("01/01/2022"),
                parseDate("01/01/2023") // Past date for expired warranty
        );
    }

    public static Service createMinimalService() {
        return createMinimal(
                "Monitor",
                "LG",
                "Osasco",
                "Tela com dead pixels",
                SituacaoServico.AGUARDANDO_AGENDAMENTO,
                null,
                null,
                ClientFactory.createDefault(),
                null
        );
    }

    // ServicoRequest creation methods
    public static ServicoRequest createValidServiceRequest(Integer clientId, Integer technicianId) {
        return new ServicoRequest(
                clientId,
                technicianId,
                "Lava Roupa",
                "Brastemp",
                "Carapicuíba",
                "05/07/2023",
                "TARDE",
                "Máquina não está centrifugando corretamente e faz barulho estranho"
        );
    }

    public static ServicoRequest createServiceRequestWithNullClientId() {
        return new ServicoRequest(
                null,
                1,
                "Geladeira",
                "Electrolux",
                "Barueri",
                "10/07/2023",
                "MANHA",
                "Não está gelando adequadamente"
        );
    }

    public static ServicoRequest createServiceRequestWithShortDescription() {
        return new ServicoRequest(
                1,
                1,
                "TV",
                "Samsung",
                "Jandira",
                "15/07/2023",
                "TARDE",
                "Não liga" // Less than 10 chars
        );
    }

    public static ServicoRequest createServiceRequestWithFewWords() {
        return new ServicoRequest(
                1,
                1,
                "Ar Condicionado",
                "LG",
                "Cotia",
                "20/07/2023",
                "MANHA",
                "Não esfria" // Only 2 words
        );
    }

    public static ServicoRequest createServiceRequestMissingRequiredFields() {
        return new ServicoRequest(
                1,
                1,
                null, // Missing equipment
                null, // Missing brand
                null, // Missing branch
                null,
                null,
                null  // Missing description
        );
    }

    public static ServicoRequest createServiceRequestWithInvalidHorario() {
        return new ServicoRequest(
                1,
                1,
                "Microondas",
                "Panasonic",
                "Santana de Parnaíba",
                "25/07/2023",
                "NOITE", // Invalid value
                "Não aquece os alimentos uniformemente"
        );
    }

    // ServicoResponse creation methods
    public static ServicoResponse createValidServicoResponse() {
        return new ServicoResponse(
                1,
                1,
                1,
                "João Silva",
                "Carlos Silva",
                "Notebook",
                "São Paulo",
                "TARDE",
                "Dell",
                "Problema com teclado",
                FormaPagamento.BOLETO.getFormaPagamento(),
                SituacaoServico.AGUARDANDO_ATENDIMENTO,
                false,
                500.0,
                50.0,
                100.0,
                parseDate("05/07/2023"),
                parseDate("10/07/2023"),
                parseDate("11/07/2023"),
                parseDate("11/08/2023"),
                parseDate("08/07/2023"),
                parseDate("15/07/2023")
        );
    }

    public static ServicoResponse createServicoResponseWithActiveWarranty() {
        return new ServicoResponse(
                2,
                2,
                2,
                "Maria Oliveira",
                "Ana Tecnica",
                "Smartphone",
                "Osasco",
                "MANHA",
                "Samsung",
                "Tela trincada",
                "PIX",
                SituacaoServico.AGUARDANDO_ATENDIMENTO,
                true, // Active warranty
                300.0,
                30.0,
                50.0,
                parseDate("01/07/2023"),
                null, // Not closed yet
                parseDate("01/07/2023"),
                parseDate("01/08/2023"),
                parseDate("05/07/2023"),
                null // Commission not paid yet
        );
    }

    public static ServicoUpdateRequest createValidServicoUpdateRequest() {
        return new ServicoUpdateRequest(
                1, // idTecnico
                1, // idCliente
                "Notebook Dell",
                "Dell",
                "São Paulo",
                "Troca de tela e reparo na placa mãe",
                SituacaoServico.AGUARDANDO_APROVACAO,
                FormaPagamento.DINHEIRO,
                "TARDE",
                500.0,
                50.0,
                100.0,
                "15/07/2023",
                "16/07/2023",
                "16/08/2023",
                "10/07/2023",
                "12/07/2023",
                "14/07/2023"
        );
    }

    public static ServicoUpdateRequest createInvalidServicoUpdateRequest() {
        return new ServicoUpdateRequest(
                null, // idTecnico missing
                null, // idCliente missing
                null, // equipamento missing
                null, // marca missing
                null, // filial missing
                "short", // descricao too short
                null, // situacao missing
                null, // formaPagamento missing
                "NOITE", // invalid horario
                -100.0, // negative valor
                -10.0, // negative comissao
                -50.0, // negative pecas
                "2023-07-15", // invalid date format
                "2023-07-16",
                "2023-08-16",
                "2023-07-10",
                "2023-07-12",
                "2023-07-14"
        );
    }

    public static ServicoUpdateRequest createServicoUpdateRequestWithNullClient() {
        return new ServicoUpdateRequest(
                1, // idTecnico
                null, // idCliente null
                "Notebook Dell",
                "Dell",
                "São Paulo",
                "Troca de tela",
                SituacaoServico.AGUARDANDO_ATENDIMENTO,
                FormaPagamento.PIX,
                "TARDE",
                500.0,
                50.0,
                100.0,
                "15/07/2023",
                "16/07/2023",
                "16/08/2023",
                "10/07/2023",
                "12/07/2023",
                "14/07/2023"
        );
    }

    public static ServicoUpdateRequest createServicoUpdateRequestWithNullTechnician() {
        return new ServicoUpdateRequest(
                null, // idTecnico null
                1, // idCliente
                "Notebook Dell",
                "Dell",
                "São Paulo",
                "Troca de tela",
                SituacaoServico.RESOLVIDO, // Status that requires technician
                FormaPagamento.DEBITO,
                "TARDE",
                500.0,
                50.0,
                100.0,
                "15/07/2023",
                "16/07/2023",
                "16/08/2023",
                "10/07/2023",
                "12/07/2023",
                "14/07/2023"
        );
    }

    public static ServicoUpdateRequest createUpdateRequestWithNegativeValue() {
        ServicoUpdateRequest valid = createValidServicoUpdateRequest();
        return new ServicoUpdateRequest(
                valid.idTecnico(),
                valid.idCliente(),
                valid.equipamento(),
                valid.marca(),
                valid.filial(),
                valid.descricao(),
                valid.situacao(),
                valid.formaPagamento(),
                valid.horarioPrevisto(),
                -100.0, // negative value
                valid.valorComissao(),
                valid.valorPecas(),
                valid.dataFechamento(),
                valid.dataInicioGarantia(),
                valid.dataFimGarantia(),
                valid.dataAtendimentoPrevisto(),
                valid.dataAtendimentoEfetiva(),
                valid.dataPagamentoComissao()
        );
    }

    public static ServicoUpdateRequest createUpdateRequestWithNegativeCommission() {
        ServicoUpdateRequest valid = createValidServicoUpdateRequest();
        return new ServicoUpdateRequest(
                valid.idTecnico(),
                valid.idCliente(),
                valid.equipamento(),
                valid.marca(),
                valid.filial(),
                valid.descricao(),
                valid.situacao(),
                valid.formaPagamento(),
                valid.horarioPrevisto(),
                valid.valor(),
                -50.0, // negative commission
                valid.valorPecas(),
                valid.dataFechamento(),
                valid.dataInicioGarantia(),
                valid.dataFimGarantia(),
                valid.dataAtendimentoPrevisto(),
                valid.dataAtendimentoEfetiva(),
                valid.dataPagamentoComissao()
        );
    }

    public static ServicoUpdateRequest createUpdateRequestWithNegativePartsValue() {
        ServicoUpdateRequest valid = createValidServicoUpdateRequest();
        return new ServicoUpdateRequest(
                valid.idTecnico(),
                valid.idCliente(),
                valid.equipamento(),
                valid.marca(),
                valid.filial(),
                valid.descricao(),
                valid.situacao(),
                valid.formaPagamento(),
                valid.horarioPrevisto(),
                valid.valor(),
                valid.valorComissao(),
                -75.0, // negative parts value
                valid.dataFechamento(),
                valid.dataInicioGarantia(),
                valid.dataFimGarantia(),
                valid.dataAtendimentoPrevisto(),
                valid.dataAtendimentoEfetiva(),
                valid.dataPagamentoComissao()
        );
    }

    public static ServicoUpdateRequest createUpdateRequestWithInvalidDate() {
        return new ServicoUpdateRequest(
                1,
                1,
                "Notebook Dell",
                "Dell",
                "São Paulo",
                "Troca de tela",
                SituacaoServico.AGUARDANDO_ATENDIMENTO,
                FormaPagamento.CREDITO,
                "TARDE",
                500.0,
                50.0,
                100.0,
                "15-07-2023", // invalid format
                "16-07-2023",
                "16-08-2023",
                "10-07-2023",
                "12-07-2023",
                "14-07-2023"
        );
    }

    public static ServicoUpdateRequest createUpdateRequestForPendingStatus() {
        return new ServicoUpdateRequest(
                null, // no technician required
                1,
                "Notebook Dell",
                "Dell",
                "São Paulo",
                "Troca de tela",
                SituacaoServico.AGUARDANDO_AGENDAMENTO, // pending status
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public static Service createValidServiceWithId(Integer id) {
        return new Service(
                id,
                "Notebook",
                "Dell",
                "São Paulo",
                "Problema com teclado",
                SituacaoServico.AGUARDANDO_ATENDIMENTO,
                "TARDE",
                500.0,
                FormaPagamento.DEBITO,
                100.0,
                50.0,
                parseDate("15/07/2023"),
                parseDate("01/07/2023"),
                parseDate("10/07/2023"),
                parseDate("11/07/2023"),
                parseDate("11/08/2023"),
                parseDate("05/07/2023"),
                parseDate("08/07/2023"),
                ClientFactory.createDefault(),
                TechnicianFactory.createDefault()
        );
    }

    public static Service createUpdatedService(Integer id, ServicoUpdateRequest request, Client client, Technician technician) {
        return new Service(
                id,
                request.equipamento(),
                request.marca(),
                request.filial(),
                request.descricao(),
                request.situacao(),
                request.horarioPrevisto(),
                request.valor(),
                request.formaPagamento(),
                request.valorPecas(),
                request.valorComissao(),
                parseDate(request.dataPagamentoComissao()),
                parseDate("15/03/2025"),
                parseDate(request.dataFechamento()),
                parseDate(request.dataInicioGarantia()),
                parseDate(request.dataFimGarantia()),
                parseDate(request.dataAtendimentoPrevisto()),
                parseDate(request.dataAtendimentoEfetiva()),
                client,
                technician
        );
    }

    public static ServicoResponse createServicoResponse(Service service) {
        Boolean garantia = null;
        if (service.getDataInicioGarantia() != null) {
            Date today = new Date();
            garantia = service.getDataInicioGarantia().before(today) &&
                    service.getDataFimGarantia().after(today);
        }

        return new ServicoResponse(
                service.getId(),
                service.getCliente().getId(),
                service.getTecnico() != null ? service.getTecnico().getId() : null,
                service.getCliente().getNome(),
                service.getTecnico() != null ?
                        service.getTecnico().getNome() + " " + service.getTecnico().getSobrenome() : null,
                service.getEquipamento(),
                service.getFilial(),
                service.getHorarioPrevisto(),
                service.getMarca(),
                service.getDescricao(),
                service.getFormaPagamento() != null ? service.getFormaPagamento().getFormaPagamento() : null,
                service.getSituacao(),
                garantia,
                service.getValor(),
                service.getValorComissao(),
                service.getValorPecas(),
                service.getDataAtendimentoPrevisto(),
                service.getDataFechamento(),
                service.getDataInicioGarantia(),
                service.getDataFimGarantia(),
                service.getDataAtendimentoEfetiva(),
                service.getDataPagamentoComissao()
        );
    }

    // Helper method to parse dates
    private static Date parseDate(String dateString) {
        if (dateString == null) {
            return null;
        }
        try {
            return DATE_FORMAT.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format in test factory", e);
        }
    }
}
