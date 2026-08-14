DROP PROCEDURE GetTabelaDisponibilidade;

CREATE PROCEDURE GetTabelaDisponibilidade(IN dia_semana int, IN especialidade_id int)
BEGIN
    WITH RECURSIVE dias AS (
        SELECT CURDATE() AS data
        UNION ALL
        SELECT DATE_ADD(data, INTERVAL 1 DAY)
        FROM dias
        WHERE data < DATE_ADD(CURDATE(), INTERVAL dia_semana DAY)
    ),
    tecnicos AS (
        SELECT DISTINCT
            t.id,
            t.nome,
            t.sobrenome
        FROM tecnico t
        INNER JOIN tecnico_especialidade te ON te.id_tecnico = t.id
        WHERE t.situacao = 'ATIVO'
            AND te.id_especialidade = 1
    )
    SELECT
        t.id AS Id,
        DAYOFWEEK(d.data) AS Dia,
        d.data AS Data,
        s.horario_previsto AS Periodo,
        CONCAT(t.nome, ' ', t.sobrenome) AS Nome,
        COUNT(s.id) AS Quantidade
    FROM tecnicos t
    CROSS JOIN dias d
    LEFT JOIN servico s ON s.id_tecnico = t.id
        AND DATE(s.data_atendimento_previsto) = d.data
    GROUP BY
        t.id,
        t.nome,
        t.sobrenome,
        d.data,
        s.horario_previsto
    ORDER BY
        t.id,
        d.data,
        s.horario_previsto;
END;