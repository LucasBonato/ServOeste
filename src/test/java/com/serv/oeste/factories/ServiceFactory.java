package com.serv.oeste.factories;

import com.serv.oeste.application.dtos.reponses.ServicoResponse;
import com.serv.oeste.application.dtos.requests.ServicoRequest;
import com.serv.oeste.application.dtos.requests.ServicoUpdateRequest;
import com.serv.oeste.domain.entities.client.Client;
import com.serv.oeste.domain.entities.service.Service;
import com.serv.oeste.domain.entities.technician.Technician;
import com.serv.oeste.domain.enums.FormaPagamento;
import com.serv.oeste.domain.enums.HorarioPrevisto;
import com.serv.oeste.domain.enums.SituacaoServico;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ServiceFactory {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static Service create(
            Integer id,
            String equipamento,
            String marca,
            String filial,
            String descricao,
            SituacaoServico situacao,
            HorarioPrevisto horarioPrevisto,
            Double valor,
            FormaPagamento formaPagamento,
            Double valorPecas,
            Double valorComissao,
            LocalDate dataPagamentoComissao,
            LocalDate dataAbertura,
            LocalDate dataFechamento,
            LocalDate dataInicioGarantia,
            LocalDate dataFimGarantia,
            LocalDate dataAtendimentoPrevisto,
            LocalDate dataAtendimentoEfetiva,
            Client cliente,
            Technician tecnico
    ) {
        return Service.restore(
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
            HorarioPrevisto horarioPrevisto,
            LocalDate dataAtendimento,
            Client cliente,
            Technician tecnico
    ) {
        return Service.create(
                equipamento,
                marca,
                filial,
                descricao,
                horarioPrevisto,
                dataAtendimento,
                cliente,
                tecnico
        );
    }

    public static Service createWithGarantia(LocalDate dataInicioGarantia, LocalDate dataFimGarantia) {
        return create(
                99,
                "Monitor",
                "Samsung",
                "Osasco",
                "Troca de tela",
                SituacaoServico.AGUARDANDO_ATENDIMENTO,
                HorarioPrevisto.TARDE,
                250.0,
                FormaPagamento.PIX,
                100.0,
                25.0,
                LocalDate.now(),
                LocalDate.now(),
                LocalDate.now(),
                dataInicioGarantia,
                dataFimGarantia,
                LocalDate.now(),
                LocalDate.now(),
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
                HorarioPrevisto.TARDE,
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
                parseDate("05/07/2023"),
                HorarioPrevisto.TARDE,
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
                parseDate("10/07/2023"),
                HorarioPrevisto.MANHA,
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
                parseDate("15/07/2023"),
                HorarioPrevisto.TARDE,
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
                parseDate("20/07/2023"),
                HorarioPrevisto.MANHA,
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
                parseDate("25/07/2023"),
                null, // Invalid value
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
                HorarioPrevisto.TARDE,
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
                HorarioPrevisto.MANHA,
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
                HorarioPrevisto.TARDE,
                500.0,
                50.0,
                100.0,
                parseDate("15/07/2023"),
                parseDate("16/07/2023"),
                parseDate("16/08/2023"),
                parseDate("10/07/2023"),
                parseDate("12/07/2023"),
                parseDate("14/07/2023")
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
                null, // invalid horario
                -100.0, // negative valor
                -10.0, // negative comissao
                -50.0, // negative pecas
                parseDate("2023-07-15"), // invalid date format
                parseDate("2023-07-16"),
                parseDate("2023-08-16"),
                parseDate("2023-07-10"),
                parseDate("2023-07-12"),
                parseDate("2023-07-14")
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
                HorarioPrevisto.TARDE,
                500.0,
                50.0,
                100.0,
                parseDate("15/07/2023"),
                parseDate("16/07/2023"),
                parseDate("16/08/2023"),
                parseDate("10/07/2023"),
                parseDate("12/07/2023"),
                parseDate("14/07/2023")
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
                HorarioPrevisto.TARDE,
                500.0,
                50.0,
                100.0,
                parseDate("15/07/2023"),
                parseDate("16/07/2023"),
                parseDate("16/08/2023"),
                parseDate("10/07/2023"),
                parseDate("12/07/2023"),
                parseDate("14/07/2023")
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
                HorarioPrevisto.TARDE,
                500.0,
                50.0,
                100.0,
                parseDate("15-07-2023"), // invalid format
                parseDate("16-07-2023"),
                parseDate("16-08-2023"),
                parseDate("10-07-2023"),
                parseDate("12-07-2023"),
                parseDate("14-07-2023")
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
        return Service.restore(
                id,
                "Notebook",
                "Dell",
                "São Paulo",
                "Problema com teclado",
                SituacaoServico.AGUARDANDO_ATENDIMENTO,
                HorarioPrevisto.TARDE,
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
        return Service.restore(
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
                request.dataPagamentoComissao(),
                parseDate("15/03/2025"),
                request.dataFechamento(),
                request.dataInicioGarantia(),
                request.dataFimGarantia(),
                request.dataAtendimentoPrevisto(),
                request.dataAtendimentoEfetiva(),
                client,
                technician
        );
    }

    public static ServicoResponse createServicoResponse(Service service) {
        Boolean garantia = null;
        if (service.getDataInicioGarantia() != null) {
            LocalDate today = LocalDate.now();
            garantia = service.getDataInicioGarantia().isBefore(today) &&
                    service.getDataFimGarantia().isAfter(today);
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

    private static LocalDate parseDate(String date) {
        return LocalDate.parse(date, DATE_FORMAT);
    }
}
