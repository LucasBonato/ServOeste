package com.serv.oeste.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.serv.oeste.domain.enums.SituacaoServico.*;
import static org.junit.jupiter.api.Assertions.*;

class SituacaoServicoTest {

    // ------------------------------------------------------------------
    // Specification tables (written by hand, NOT derived from the enum)
    // ------------------------------------------------------------------

    private static final Map<SituacaoServico, Set<SituacaoServico>> EXPECTED_PROXIMOS =
            new EnumMap<>(SituacaoServico.class);

    static {
        EXPECTED_PROXIMOS.put(AGUARDANDO_AGENDAMENTO, EnumSet.of(AGUARDANDO_ATENDIMENTO));
        EXPECTED_PROXIMOS.put(AGUARDANDO_ATENDIMENTO, EnumSet.of(AGUARDANDO_APROVACAO, SEM_DEFEITO, CANCELADO));
        EXPECTED_PROXIMOS.put(AGUARDANDO_APROVACAO, EnumSet.of(NAO_APROVADO, COMPRA, ORCAMENTO_APROVADO));
        EXPECTED_PROXIMOS.put(ORCAMENTO_APROVADO, EnumSet.of(AGUARDANDO_CLIENTE_RETIRAR));
        EXPECTED_PROXIMOS.put(AGUARDANDO_CLIENTE_RETIRAR, EnumSet.of(GARANTIA, NAO_RETIRA_3_MESES));
        EXPECTED_PROXIMOS.put(GARANTIA, EnumSet.of(CORTESIA, RESOLVIDO));
        EXPECTED_PROXIMOS.put(NAO_RETIRA_3_MESES, EnumSet.of(RESOLVIDO));
        EXPECTED_PROXIMOS.put(CANCELADO, EnumSet.noneOf(SituacaoServico.class));
        EXPECTED_PROXIMOS.put(COMPRA, EnumSet.noneOf(SituacaoServico.class));
        EXPECTED_PROXIMOS.put(CORTESIA, EnumSet.of(RESOLVIDO));
        EXPECTED_PROXIMOS.put(NAO_APROVADO, EnumSet.noneOf(SituacaoServico.class));
        EXPECTED_PROXIMOS.put(RESOLVIDO, EnumSet.noneOf(SituacaoServico.class));
        EXPECTED_PROXIMOS.put(SEM_DEFEITO, EnumSet.noneOf(SituacaoServico.class));
    }

    private static final Set<SituacaoServico> INICIAIS =
            EnumSet.of(AGUARDANDO_AGENDAMENTO, AGUARDANDO_ATENDIMENTO);

    private static final Set<SituacaoServico> EXIGE_DESCRICAO = EnumSet.of(
            AGUARDANDO_AGENDAMENTO, AGUARDANDO_ATENDIMENTO, AGUARDANDO_APROVACAO,
            CANCELADO, CORTESIA, NAO_APROVADO, SEM_DEFEITO);

    private static final Set<SituacaoServico> EXIGE_FORMA_PAGAMENTO = EnumSet.of(
            ORCAMENTO_APROVADO, AGUARDANDO_CLIENTE_RETIRAR, GARANTIA, NAO_RETIRA_3_MESES,
            COMPRA, CORTESIA, NAO_APROVADO, RESOLVIDO);

    private static final Set<SituacaoServico> EXIGE_VALOR_SERVICO = EnumSet.of(
            AGUARDANDO_APROVACAO, NAO_APROVADO, COMPRA, ORCAMENTO_APROVADO,
            AGUARDANDO_CLIENTE_RETIRAR, NAO_RETIRA_3_MESES, RESOLVIDO, CORTESIA, GARANTIA);

    private static final Set<SituacaoServico> EXIGE_DATA_FECHAMENTO =
            EnumSet.of(RESOLVIDO, CORTESIA, GARANTIA);

    private static final Set<SituacaoServico> EXIGE_PAGAMENTO_COMISSAO =
            EnumSet.of(CANCELADO, NAO_APROVADO, COMPRA, RESOLVIDO);

    private static final Set<SituacaoServico> EXIGE_FIM_GARANTIA =
            EnumSet.of(RESOLVIDO, CORTESIA, GARANTIA);

    // Everything except AGUARDANDO_AGENDAMENTO
    private static final Set<SituacaoServico> EXIGE_ATENDIMENTO_PREVISTO =
            EnumSet.complementOf(EnumSet.of(AGUARDANDO_AGENDAMENTO, CANCELADO));

    // Everything except the two initial situations
    private static final Set<SituacaoServico> EXIGE_ATENDIMENTO_EFETIVO =
            EnumSet.complementOf(EnumSet.of(AGUARDANDO_AGENDAMENTO, AGUARDANDO_ATENDIMENTO, CANCELADO));

    // ------------------------------------------------------------------
    // Transition graph
    // ------------------------------------------------------------------

    @Test
    @DisplayName("Spec table covers every enum value (fails when a new situation is added without a spec)")
    void expectedTable_ShouldCoverAllSituations() {
        assertEquals(EnumSet.allOf(SituacaoServico.class), EXPECTED_PROXIMOS.keySet());
    }

    @ParameterizedTest(name = "{0}.proximos() matches the spec")
    @EnumSource(SituacaoServico.class)
    void proximos_ShouldMatchSpec(SituacaoServico origem) {
        assertEquals(EXPECTED_PROXIMOS.get(origem), origem.proximos());
    }

    @ParameterizedTest(name = "{0} has no self-loops and proximos/anteriores are disjoint")
    @EnumSource(SituacaoServico.class)
    void transitions_ShouldNotHaveSelfLoopsOrOverlap(SituacaoServico origem) {
        Set<SituacaoServico> proximos = origem.proximos();
        Set<SituacaoServico> anteriores = origem.anteriores();

        assertFalse(proximos.contains(origem), origem + " must not advance to itself");
        assertFalse(anteriores.contains(origem), origem + " must not return to itself");

        Set<SituacaoServico> overlap = new HashSet<>(proximos);
        overlap.retainAll(anteriores);
        assertTrue(overlap.isEmpty(), origem + " has the same situations in proximos() and anteriores(): " + overlap);
    }

    @ParameterizedTest(name = "{0}: proximos() and anteriores() mirror each other")
    @EnumSource(SituacaoServico.class)
    void proximosAndAnteriores_ShouldMirrorEachOther(SituacaoServico origem) {
        for (SituacaoServico proximo : origem.proximos()) {
            assertTrue(
                    proximo.anteriores().contains(origem),
                    String.format("%s.proximos() contains %s, but %s.anteriores() does not contain %s",
                            origem, proximo, proximo, origem));
        }
        // The reverse direction was not checked by the original test
        for (SituacaoServico anterior : origem.anteriores()) {
            assertTrue(
                    anterior.proximos().contains(origem),
                    String.format("%s.anteriores() contains %s, but %s.proximos() does not contain %s",
                            origem, anterior, anterior, origem)
            );
        }
    }

    @Test
    @DisplayName("podeAvancarPara / podeRetornarPara agree with the spec for every pair")
    void canAdvanceAndReturn_ShouldAgreeWithSpecForEveryPair() {
        for (SituacaoServico origem : SituacaoServico.values()) {
            for (SituacaoServico destino : SituacaoServico.values()) {
                boolean expectedAdvance = EXPECTED_PROXIMOS.get(origem).contains(destino);
                boolean expectedReturn = EXPECTED_PROXIMOS.get(destino).contains(origem);

                assertEquals(expectedAdvance, origem.podeAvancarPara(destino),
                        origem + " -> " + destino + " (advance)");
                assertEquals(expectedReturn, origem.podeRetornarPara(destino),
                        origem + " -> " + destino + " (return)");
            }
        }
    }

    @Test
    @DisplayName("Only the initial situations have no anteriores")
    void anteriores_ShouldBeEmptyOnlyForAguardandoAgendamento() {
        for (SituacaoServico situacao : SituacaoServico.values()) {
            assertEquals(
                    situacao == AGUARDANDO_AGENDAMENTO,
                    situacao.anteriores().isEmpty(),
                    situacao + ".anteriores().isEmpty()");
        }
    }

    @Test
    @DisplayName("Every situation is reachable from AGUARDANDO_AGENDAMENTO")
    void everySituation_ShouldBeReachableFromTheFirstOne() {
        Set<SituacaoServico> visited = EnumSet.of(AGUARDANDO_AGENDAMENTO);
        Deque<SituacaoServico> queue = new ArrayDeque<>(visited);

        while (!queue.isEmpty()) {
            for (SituacaoServico next : queue.poll().proximos()) {
                if (visited.add(next)) {
                    queue.add(next);
                }
            }
        }

        assertEquals(EnumSet.allOf(SituacaoServico.class), visited,
                "Unreachable situations: " + EnumSet.complementOf(EnumSet.copyOf(visited)));
    }

    @Test
    @DisplayName("Terminal situations (no next step) are exactly the expected ones")
    void terminalSituations_ShouldBeExactlyTheExpectedOnes() {
        Set<SituacaoServico> terminals = EnumSet.noneOf(SituacaoServico.class);
        for (SituacaoServico situacao : SituacaoServico.values()) {
            if (situacao.proximos().isEmpty()) {
                terminals.add(situacao);
            }
        }

        assertEquals(
                EnumSet.of(CANCELADO, COMPRA, NAO_APROVADO, RESOLVIDO, SEM_DEFEITO),
                terminals);
    }

    // ------------------------------------------------------------------
    // Initial situations
    // ------------------------------------------------------------------

    @ParameterizedTest(name = "isInicial({0})")
    @EnumSource(SituacaoServico.class)
    void isInicial_ShouldAcceptOnlyAgendamentoAndAtendimento(SituacaoServico situacao) {
        assertEquals(INICIAIS.contains(situacao), SituacaoServico.isInicial(situacao));
    }

    // ------------------------------------------------------------------
    // Business rules (one parameterized test per rule)
    // ------------------------------------------------------------------

    @ParameterizedTest(name = "exigeDescricao: {0}")
    @EnumSource(SituacaoServico.class)
    void exigeDescricao_ShouldMatchSpec(SituacaoServico situacao) {
        assertEquals(EXIGE_DESCRICAO.contains(situacao), situacao.exigeDescricao());
    }

    @ParameterizedTest(name = "exigeFormaPagamento: {0}")
    @EnumSource(SituacaoServico.class)
    void exigeFormaPagamento_ShouldMatchSpec(SituacaoServico situacao) {
        assertEquals(EXIGE_FORMA_PAGAMENTO.contains(situacao), situacao.exigeFormaPagamento());
    }

    @ParameterizedTest(name = "exigeValorServico: {0}")
    @EnumSource(SituacaoServico.class)
    void exigeValorServico_ShouldMatchSpec(SituacaoServico situacao) {
        assertEquals(EXIGE_VALOR_SERVICO.contains(situacao), situacao.exigeValorServico());
    }

    @ParameterizedTest(name = "exigeDataFechamento: {0}")
    @EnumSource(SituacaoServico.class)
    void exigeDataFechamento_ShouldMatchSpec(SituacaoServico situacao) {
        assertEquals(EXIGE_DATA_FECHAMENTO.contains(situacao), situacao.exigeDataFechamento());
    }

    @ParameterizedTest(name = "exigePagamentoComissao: {0}")
    @EnumSource(SituacaoServico.class)
    void exigePagamentoComissao_ShouldMatchSpec(SituacaoServico situacao) {
        assertEquals(EXIGE_PAGAMENTO_COMISSAO.contains(situacao), situacao.exigePagamentoComissao());
    }

    @ParameterizedTest(name = "exigeFimGarantia: {0}")
    @EnumSource(SituacaoServico.class)
    void exigeFimGarantia_ShouldMatchSpec(SituacaoServico situacao) {
        assertEquals(EXIGE_FIM_GARANTIA.contains(situacao), situacao.exigeFimGarantia());
    }

    @ParameterizedTest(name = "exigeAtendimentoPrevisto: {0}")
    @EnumSource(SituacaoServico.class)
    void exigeAtendimentoPrevisto_ShouldMatchSpec(SituacaoServico situacao) {
        assertEquals(EXIGE_ATENDIMENTO_PREVISTO.contains(situacao), situacao.exigeAtendimentoPrevisto());
    }

    @ParameterizedTest(name = "exigeAtendimentoEfetivo: {0}")
    @EnumSource(SituacaoServico.class)
    void exigeAtendimentoEfetivo_ShouldMatchSpec(SituacaoServico situacao) {
        assertEquals(EXIGE_ATENDIMENTO_EFETIVO.contains(situacao), situacao.exigeAtendimentoEfetivo());
    }

    // ------------------------------------------------------------------
    // Cross-rule consistency
    // ------------------------------------------------------------------

    @ParameterizedTest(name = "{0}: efective attendance implies expected attendance")
    @EnumSource(SituacaoServico.class)
    void exigeAtendimentoEfetivo_ShouldImplyExigeAtendimentoPrevisto(SituacaoServico situacao) {
        if (situacao.exigeAtendimentoEfetivo()) {
            assertTrue(situacao.exigeAtendimentoPrevisto(),
                    situacao + " requires effective attendance date but not the expected one");
        }
    }

    @ParameterizedTest(name = "{0}: closing date implies payment method")
    @EnumSource(SituacaoServico.class)
    void exigeDataFechamento_ShouldImplyExigeFormaPagamento(SituacaoServico situacao) {
        if (situacao.exigeDataFechamento()) {
            assertTrue(situacao.exigeFormaPagamento(),
                    situacao + " closes the service without requiring a payment method");
        }
    }

    @ParameterizedTest(name = "{0}: initial situations never require closing data")
    @EnumSource(value = SituacaoServico.class, names = {"AGUARDANDO_AGENDAMENTO", "AGUARDANDO_ATENDIMENTO"})
    void initialSituations_ShouldNotRequireClosingData(SituacaoServico situacao) {
        assertAll(
                () -> assertFalse(situacao.exigeDataFechamento()),
                () -> assertFalse(situacao.exigeFormaPagamento()),
                () -> assertFalse(situacao.exigeValorServico()),
                () -> assertFalse(situacao.exigePagamentoComissao()),
                () -> assertFalse(situacao.exigeFimGarantia()),
                () -> assertFalse(situacao.exigeAtendimentoEfetivo())
        );
    }

    // ------------------------------------------------------------------
    // Labels
    // ------------------------------------------------------------------

    @Test
    @DisplayName("getSituacao() returns a non-blank, unique label for every situation")
    void getSituacao_ShouldBeNonBlankAndUnique() {
        Set<String> labels = new HashSet<>();

        for (SituacaoServico situacao : SituacaoServico.values()) {
            String label = situacao.getSituacao();
            assertNotNull(label, situacao + " has a null label");
            assertFalse(label.isBlank(), situacao + " has a blank label");
            assertTrue(labels.add(label), "Duplicated label: " + label);
        }
    }

    @Test
    void getSituacao_ShouldReturnTheHumanReadableLabel() {
        assertAll(
                () -> assertEquals("Aguardando agendamento", AGUARDANDO_AGENDAMENTO.getSituacao()),
                () -> assertEquals("Aguardando atendimento", AGUARDANDO_ATENDIMENTO.getSituacao()),
                () -> assertEquals("Aguardando aprovação do cliente", AGUARDANDO_APROVACAO.getSituacao()),
                () -> assertEquals("Orçamento aprovado", ORCAMENTO_APROVADO.getSituacao()),
                () -> assertEquals("Aguardando cliente retirar", AGUARDANDO_CLIENTE_RETIRAR.getSituacao()),
                () -> assertEquals("Garantia", GARANTIA.getSituacao()),
                () -> assertEquals("Não retira há 3 meses", NAO_RETIRA_3_MESES.getSituacao()),
                () -> assertEquals("Cancelado", CANCELADO.getSituacao()),
                () -> assertEquals("Compra", COMPRA.getSituacao()),
                () -> assertEquals("Cortesia", CORTESIA.getSituacao()),
                () -> assertEquals("Não aprovado pelo cliente", NAO_APROVADO.getSituacao()),
                () -> assertEquals("Resolvido", RESOLVIDO.getSituacao()),
                () -> assertEquals("Sem defeito", SEM_DEFEITO.getSituacao())
        );
    }
}