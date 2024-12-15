CREATE PROCEDURE GetTabelaDisponibilidade(IN dia_semana int, IN especialidade_id int)
BEGIN
	SELECT
		t.id AS 'Id',
		DAYOFWEEK(s.data_atendimento_previsto) AS 'Dia',
		s.data_atendimento_previsto AS 'Data',
		s.horario_previsto AS 'Periodo',
		CONCAT(t.nome, ' ', t.sobrenome) AS 'Nome',
		COUNT(s.id_tecnico) AS 'Quantidade'
	FROM servico s
	INNER JOIN tecnico t ON t.id = s.id_tecnico
	INNER JOIN tecnico_especialidade te ON te.id_tecnico = t.id
	INNER JOIN especialidade e ON e.id = especialidade_id
	WHERE s.data_atendimento_previsto BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL dia_semana DAY)
	AND t.situacao = 'ATIVO'
	GROUP BY t.nome, t.sobrenome, s.horario_previsto, s.data_atendimento_previsto, t.id;
END;