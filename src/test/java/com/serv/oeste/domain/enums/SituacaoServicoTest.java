package com.serv.oeste.domain.enums;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class SituacaoServicoTest {

    @Test
    void transitionGraph_ProximosAndAnteriores_ShouldBeSymmetricDisjointAndWithoutSelfLoops() {
        // Arrange & Act & Assert
        for (SituacaoServico origem : SituacaoServico.values()) {
            Set<SituacaoServico> proximos = origem.proximos();
            Set<SituacaoServico> anteriores = origem.anteriores();

            assertFalse(
                    proximos.contains(origem),
                    origem + " não pode transitar para si mesma via proximos()"
            );
            assertFalse(
                    anteriores.contains(origem),
                    origem + " não pode transitar para si mesma via anteriores()"
            );

            Set<SituacaoServico> intersection = proximos.stream()
                    .filter(anteriores::contains)
                    .collect(Collectors.toSet());
            assertTrue(
                    intersection.isEmpty(),
                    origem + " possui situações em proximos() e anteriores() ao mesmo tempo: " + intersection
            );

            for (SituacaoServico proximo : proximos) {
                assertTrue(
                        proximo.anteriores().contains(origem),
                        String.format(
                                "%s.proximos() contém %s, mas %s.anteriores() não contém %s",
                                origem, proximo, proximo, origem
                        )
                );
                assertTrue(origem.podeAvancarPara(proximo));
                assertFalse(proximo.podeAvancarPara(origem), "Transição inversa não deveria ser permitida");
            }

            for (SituacaoServico anterior : anteriores) {
                assertTrue(origem.podeRetornarPara(anterior));
            }
        }
    }

    @Test
    void exigeDescricao_ShouldRequireDescriptionOnlyForDocumentedSituations() {
        // Arrange
        Set<SituacaoServico> expected = Set.of(
                SituacaoServico.AGUARDANDO_AGENDAMENTO,
                SituacaoServico.AGUARDANDO_ATENDIMENTO,
                SituacaoServico.AGUARDANDO_APROVACAO,
                SituacaoServico.CANCELADO,
                SituacaoServico.CORTESIA,
                SituacaoServico.NAO_APROVADO,
                SituacaoServico.RESOLVIDO,
                SituacaoServico.SEM_DEFEITO
        );

        // Act & Assert
        for (SituacaoServico situacao : SituacaoServico.values()) {
            assertEquals(
                    expected.contains(situacao),
                    situacao.exigeDescricao(),
                    situacao.name() + ".exigeDescricao() deveria ser " + expected.contains(situacao)
            );
        }
    }

    @Test
    void isInicial_ShouldAcceptOnlyAgendamentoAndAtendimento() {
        // Arrange
        SituacaoServico[] expectedInicial = {
                SituacaoServico.AGUARDANDO_AGENDAMENTO,
                SituacaoServico.AGUARDANDO_ATENDIMENTO
        };

        // Act & Assert
        for (SituacaoServico situacao : SituacaoServico.values()) {
            boolean isExpected = situacao == SituacaoServico.AGUARDANDO_AGENDAMENTO
                    || situacao == SituacaoServico.AGUARDANDO_ATENDIMENTO;
            assertTrue(
                    SituacaoServico.isInicial(situacao) == isExpected,
                    situacao.name() + " deveria ser inicial = " + isExpected
            );
        }
    }
}