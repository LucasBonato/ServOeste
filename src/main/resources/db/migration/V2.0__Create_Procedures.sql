CREATE PROCEDURE GetTabelaDisponibilidade(IN dia_semana int, IN especialidade_id int)
BEGIN
	SELECT
		t.id AS 'id',
		DAYOFWEEK(s.data_atendimento_previsto) AS 'dia',
		s.data_atendimento_previsto AS 'data',
		s.horario_previsto AS 'periodo',
		CONCAT(t.nome, ' ', t.sobrenome) AS 'nome',
		COUNT(s.id_tecnico) AS 'quantidade'
	FROM servico s
	INNER JOIN tecnico t ON t.id = s.id_tecnico
	INNER JOIN tecnico_especialidade te ON te.id_tecnico = t.id
	INNER JOIN especialidade e ON e.id = te.id_especialidade
	WHERE s.data_atendimento_previsto BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL dia_semana DAY)
	AND t.situacao = 'ATIVO'
    AND e.id = especialidade_id
	GROUP BY t.nome, t.sobrenome, s.horario_previsto, s.data_atendimento_previsto, t.id;
END;