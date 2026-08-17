	USE servOeste;

	-- Drop staging tables if exist
	DROP TABLE IF EXISTS stg_tecnico;
	DROP TABLE IF EXISTS stg_tecnico_especialidade;
	DROP TABLE IF EXISTS stg_cliente;
	DROP TABLE IF EXISTS stg_servico;

	-- 1. Staging for tecnicos
	CREATE TABLE stg_tecnico (
		col1 TEXT, -- Id
		col2 TEXT, -- Telefone Fixo
		col3 TEXT, -- Telefone Celular
		col4 TEXT, -- Nome
		col5 TEXT, -- Sobrenome
		col6 TEXT  -- Situação
	);

	-- 2. Staging for tecnico_especialidade
	CREATE TABLE stg_tecnico_especialidade (
		col1 TEXT, -- Id Especialidade
		col2 TEXT  -- Id Tecnico
	);

	-- 3. Staging for clientes
	CREATE TABLE stg_cliente (
		col1 TEXT, -- Id
		col2 TEXT, -- Nome
		col3 TEXT, -- Telefone Fixo
		col4 TEXT, -- Telefone Celular
		col5 TEXT, -- Endereço
		col6 TEXT, -- Bairro
		col7 TEXT  -- municipio
	);

	-- 4. Staging for servicos
	CREATE TABLE stg_servico (
		col1 TEXT,   -- Id
		col2 TEXT,   -- Equipamento
		col3 TEXT,   -- Marca
		col4 TEXT,   -- Descrição
		col5 TEXT,   -- Data_Atendimento_Previsto
		col6 TEXT,   -- Horario_Previsto
		col7 TEXT,   -- Id Técnico
		col8 TEXT,   -- Id Cliente
		col9 TEXT,   -- Situação
		col10 TEXT,  -- Data_Fechamento
		col11 TEXT,  -- Data_Abertura
		col12 TEXT,  -- Data_Inicio_Garantia
		col13 TEXT,  -- Data_Fim_Garantia
		col14 TEXT,  -- Valor
		col15 TEXT,  -- Data_Atendimento_Efetiva
		col16 TEXT,  -- Forma_Pagamento
		col17 TEXT,  -- Filial ("Serv Oeste" == "Osasco"; "Nikitin" == "Curitiba")
		col18 TEXT,  -- Valor_Peca
		col19 TEXT,  -- Valor_Comissão
		col20 TEXT   -- Data_Pagamento_Comissão
	);

	LOAD DATA INFILE '/var/lib/mysql-files/tecnicos.csv'
	INTO TABLE stg_tecnico
	CHARACTER SET utf8mb4
	FIELDS TERMINATED BY ';'
	ENCLOSED BY '"'
	ESCAPED BY '\\'
	LINES TERMINATED BY '\r\n';

	LOAD DATA INFILE '/var/lib/mysql-files/tec_esp.csv'
	INTO TABLE stg_tecnico_especialidade
	CHARACTER SET utf8mb4
	FIELDS TERMINATED BY ';'
	ENCLOSED BY '"'
	ESCAPED BY '\\'
	LINES TERMINATED BY '\r\n';

	LOAD DATA INFILE '/var/lib/mysql-files/clientes.csv'
	INTO TABLE stg_cliente
	CHARACTER SET utf8mb4
	FIELDS TERMINATED BY ';'
	ENCLOSED BY '"'
	ESCAPED BY '\\'
	LINES TERMINATED BY '\r\n';

	LOAD DATA INFILE '/var/lib/mysql-files/servicos.csv'
	INTO TABLE stg_servico
	CHARACTER SET utf8mb4
	FIELDS TERMINATED BY ';'
	ENCLOSED BY '"'
	ESCAPED BY '\\'
	LINES TERMINATED BY '\r\n';

	UPDATE stg_cliente
	SET col1 = REGEXP_REPLACE(col1, '^[^0-9]+', '');

	UPDATE stg_servico
	SET col1 = REGEXP_REPLACE(col1, '^[^0-9]+', '');

	DELETE FROM stg_tecnico WHERE col1 = '1';

	-- (3) drop and recreate helper functions (if not already)
	DROP FUNCTION IF EXISTS FormatTelefone;
	DROP FUNCTION IF EXISTS GetFirstName;
	DROP FUNCTION IF EXISTS GetSurname;
	DROP PROCEDURE IF EXISTS migrar_clientes;
	DROP PROCEDURE IF EXISTS migrar_tecnicos;

	DELIMITER $$

	CREATE FUNCTION FormatTelefone(telefone VARCHAR(255))
	RETURNS VARCHAR(20)
	DETERMINISTIC
	BEGIN
		DECLARE clean VARCHAR(20);
		IF telefone IS NULL THEN RETURN NULL; END IF;
		SET clean = REGEXP_REPLACE(telefone, '[^0-9]', '');
		IF clean = '' THEN RETURN NULL; END IF;
		RETURN clean;
	END$$

	CREATE FUNCTION GetFirstName(fullname VARCHAR(255))
	RETURNS VARCHAR(255)
	DETERMINISTIC
	BEGIN
		DECLARE name_clean VARCHAR(255);
		IF fullname IS NULL OR TRIM(fullname) = '' THEN RETURN NULL; END IF;
		SET name_clean = TRIM(fullname);
		IF name_clean LIKE '%/%' THEN RETURN name_clean; END IF;
		IF name_clean NOT LIKE '% %' THEN RETURN name_clean; END IF;
		RETURN SUBSTRING_INDEX(name_clean, ' ', 1);
	END$$

	CREATE FUNCTION GetSurname(fullname VARCHAR(255))
	RETURNS VARCHAR(255)
	DETERMINISTIC
	BEGIN
		DECLARE name_clean VARCHAR(255);
		IF fullname IS NULL OR TRIM(fullname) = '' THEN RETURN ''; END IF;
		SET name_clean = TRIM(fullname);
		IF name_clean LIKE '%/%' THEN RETURN ''; END IF;
		IF name_clean NOT LIKE '% %' THEN RETURN ''; END IF;
		RETURN TRIM(SUBSTRING(name_clean FROM LENGTH(SUBSTRING_INDEX(name_clean, ' ', 1)) + 2));
	END$$

	DELIMITER ;

	-- (4) Upsert cliente utilizando old_id
	INSERT INTO cliente (
		old_id,
		telefone_fixo,
		telefone_celular,
		nome,
		bairro,
		endereco,
		municipio
	)
	SELECT
		*
	FROM (
		SELECT
			CAST(col1 AS UNSIGNED) AS old_id,
			CASE
				WHEN LENGTH(FormatTelefone(col3)) = 8  THEN CONCAT('11', FormatTelefone(col3))
				WHEN LENGTH(FormatTelefone(col3)) = 10 THEN FormatTelefone(col3)
				ELSE NULL
			END AS telefone_fixo,
			CASE
				WHEN LENGTH(FormatTelefone(col4)) = 11 THEN FormatTelefone(col4)
				ELSE NULL
			END AS telefone_celular,
			TRIM(col2) AS nome,
			TRIM(col6) AS bairro,
			TRIM(col5) AS endereco,
			TRIM(col7) AS municipio
		FROM stg_cliente
	) AS new
	ON DUPLICATE KEY UPDATE
		telefone_fixo    = new.telefone_fixo,
		telefone_celular = new.telefone_celular,
		nome             = new.nome,
		bairro           = new.bairro,
		endereco         = new.endereco,
		municipio        = new.municipio;

	-- (5) Upsert tecnico utilizando old_id
	INSERT INTO tecnico (
		old_id,
		telefone_fixo,
		telefone_celular,
		nome,
		sobrenome,
		situacao
	)
	SELECT
		*
	FROM (
		SELECT
			CAST(col1 AS UNSIGNED) AS old_id,
			CASE WHEN LENGTH(FormatTelefone(col2)) = 10 THEN FormatTelefone(col2) ELSE NULL END AS telefone_fixo,
			CASE WHEN LENGTH(FormatTelefone(col3)) = 11 THEN FormatTelefone(col3) ELSE NULL END AS telefone_celular,
			GetFirstName(col4) AS nome,
			GetSurname(col4) AS sobrenome,
			CASE
				WHEN col6 LIKE '%Ativo%' THEN 'ATIVO'
				WHEN col6 LIKE '%Desligado%' THEN 'DESATIVADO'
				WHEN col6 LIKE '%Licen%' THEN 'LICENCA'
				ELSE 'ATIVO'
			END AS situacao
		FROM stg_tecnico
	) AS new
	ON DUPLICATE KEY UPDATE
		telefone_fixo    = new.telefone_fixo,
		telefone_celular = new.telefone_celular,
		nome             = new.nome,
		sobrenome        = new.sobrenome,
		situacao         = new.situacao;

	-- (6) Insert tecnico_especialidade using mapping
    CREATE TEMPORARY TABLE legacy_especialidade_map (
        old_id INT PRIMARY KEY,
        conhecimento VARCHAR(255) NOT NULL
    );

    INSERT INTO legacy_especialidade_map (old_id, conhecimento)
    VALUES
        (1,  'Adega'),
        (2,  'Bebedouro'),
        (3,  'Climatizador'),
        (4,  'Cooler'),
        (5,  'Frigobar'),
        (6,  'Geladeira'),
        (7,  'Lava Louça'),
        (8,  'Lava Roupa'),
        (9,  'Microondas'),
        (10, 'Purificador'),
        (11, 'Secadora'),
        (12, 'Outros');

    INSERT INTO tecnico_especialidade (
        id_especialidade,
        id_tecnico
    )
    SELECT
        e.id,
        t.id
    FROM stg_tecnico_especialidade s
        JOIN legacy_especialidade_map m ON m.old_id = CAST(REGEXP_REPLACE(s.col1, '[^0-9]', '') AS UNSIGNED)
        JOIN tecnico t ON t.old_id = CAST(REGEXP_REPLACE(s.col2, '[^0-9]', '') AS UNSIGNED)
        JOIN especialidade e ON e.conhecimento = m.conhecimento
    WHERE e.Ativo = 1
    ON DUPLICATE KEY UPDATE id_especialidade = VALUES(id_especialidade);

    DROP TEMPORARY TABLE legacy_especialidade_map;

	-- (7) Insert servico using mappings (only mapped rows)
	INSERT INTO servico (
	  old_id, id_cliente, id_tecnico, situacao, valor,
	  data_abertura, data_atendimento_efetiva, data_atendimento_previsto,
	  data_fechamento, data_fim_garantia, data_inicio_garantia,
	  descricao, equipamento, filial, horario_previsto, marca,
	  forma_pagamento, valor_pecas, valor_comissao, data_pagamento_comissao
	)
	SELECT
		*
	FROM (
		SELECT
			CAST(col1 AS UNSIGNED) AS old_id,
			c.id AS id_cliente,
			t.id AS id_tecnico,
			CASE
				WHEN col9 LIKE '%Aguardando atendimento%' THEN 'AGUARDANDO_ATENDIMENTO'
				WHEN col9 LIKE '%Cancelado%' THEN 'CANCELADO'
				WHEN col9 LIKE '%Sem defeito%' THEN 'SEM_DEFEITO'
				WHEN col9 LIKE '%Aguardando orçamento%' THEN 'AGUARDANDO_ORCAMENTO'
				WHEN col9 LIKE '%Aguardando aprovação%' THEN 'AGUARDANDO_APROVACAO'
				WHEN col9 LIKE '%Compra%' THEN 'COMPRA'
				WHEN col9 LIKE '%Não aprovado%' THEN 'NAO_APROVADO'
				WHEN col9 LIKE '%Orçamento aprovado%' THEN 'ORCAMENTO_APROVADO'
				WHEN col9 LIKE '%Aguardando cliente retirar%' THEN 'AGUARDANDO_CLIENTE_RETIRAR'
				WHEN col9 LIKE '%3 meses%' THEN 'NAO_RETIRA_3_MESES'
				WHEN col9 LIKE '%Resolvido%' THEN 'RESOLVIDO'
				WHEN col9 LIKE '%Garantia%' THEN 'GARANTIA'
				WHEN col9 LIKE '%Cortesia%' THEN 'CORTESIA'
				ELSE 'AGUARDANDO_ATENDIMENTO'
			END AS situacao,
			CAST(NULLIF(col14, '0.00') AS DECIMAL(10,2)) AS valor,
			STR_TO_DATE(NULLIF(TRIM(col11), 'NULL'), '%Y-%m-%d') AS data_abertura,
			STR_TO_DATE(NULLIF(TRIM(col15), 'NULL'), '%Y-%m-%d') AS data_atendimento_efetiva,
			STR_TO_DATE(NULLIF(TRIM(col5), 'NULL'), '%Y-%m-%d') AS data_atendimento_previsto,
			STR_TO_DATE(NULLIF(TRIM(col10), 'NULL'), '%Y-%m-%d') AS data_fechamento,
			STR_TO_DATE(NULLIF(TRIM(col13), 'NULL'), '%Y-%m-%d') AS data_fim_garantia,
			STR_TO_DATE(NULLIF(TRIM(col12), 'NULL'), '%Y-%m-%d') AS data_inicio_garantia,
			col4 AS descricao,
			col2 AS equipamento,
			CASE
				WHEN col17 LIKE '%Serv Oeste%' THEN 'Osasco'
				WHEN col17 LIKE '%Nikitin%' THEN 'Carapicuíba'
				ELSE 'OSASCO'
			END AS filial,
			CASE
				WHEN TRIM(col6) LIKE '%Manh%' THEN 'MANHA'
				WHEN TRIM(col6) LIKE '%Tarde%' THEN 'TARDE'
				WHEN TRIM(col15) NOT IN ('', 'NULL') THEN 'MANHA'
				WHEN TRIM(col5) NOT IN ('', 'NULL') THEN 'MANHA'
				ELSE NULL
			END AS horario_previsto,
			col3 AS marca,
			CASE
				WHEN col16 LIKE '%Dinheiro%' THEN 'DINHEIRO'
				WHEN col16 LIKE '%Débito%' THEN 'DEBITO'
				WHEN col16 LIKE '%Crédito%' THEN 'CREDITO'
				WHEN col16 LIKE '%Pix%' THEN 'PIX'
				WHEN col16 LIKE '%Boleto%' THEN 'BOLETO'
				ELSE NULL
			END AS forma_pagamento,
			CAST(NULLIF(col18, '0.00') AS DECIMAL(10,2)) AS valor_pecas,
			CAST(NULLIF(col19, '0.00') AS DECIMAL(10,2)) AS valor_comissao,
			CASE
				WHEN TRIM(col20) REGEXP '^[0-9]{4}-[0-9]{2}-[0-9]{2}$'
				THEN STR_TO_DATE(TRIM(col20), '%Y-%m-%d')
				ELSE NULL
			END AS data_pagamento_comissao
		FROM stg_servico
		JOIN cliente c ON CAST(col8 AS UNSIGNED) = c.old_id
		JOIN tecnico t ON CAST(col7 AS UNSIGNED) = t.old_id
	) AS new
	ON DUPLICATE KEY UPDATE
		id_cliente					= new.id_cliente,
		id_tecnico					= new.id_tecnico,
		situacao					= new.situacao,
		valor						= new.valor,
		data_abertura				= new.data_abertura,
		data_atendimento_efetiva	= new.data_atendimento_efetiva,
		data_atendimento_previsto	= new.data_atendimento_previsto,
		data_fechamento				= new.data_fechamento,
		data_fim_garantia			= new.data_fim_garantia,
		data_inicio_garantia		= new.data_inicio_garantia,
		descricao					= new.descricao,
		equipamento					= new.equipamento,
		filial						= new.filial,
		horario_previsto			= new.horario_previsto,
		marca						= new.marca,
		forma_pagamento				= new.forma_pagamento,
		valor_pecas					= new.valor_pecas,
		valor_comissao				= new.valor_comissao,
		data_pagamento_comissao		= new.data_pagamento_comissao;


	-- (8) After import, enforce ENUM columns (validate first if needed)
	ALTER TABLE servico MODIFY forma_pagamento ENUM('DEBITO', 'DINHEIRO', 'CREDITO', 'PIX', 'BOLETO');
	ALTER TABLE servico MODIFY situacao ENUM(
	'AGUARDANDO_AGENDAMENTO',
	'AGUARDANDO_ATENDIMENTO',
	'AGUARDANDO_APROVACAO',
	'AGUARDANDO_CLIENTE_RETIRAR',
	'AGUARDANDO_ORCAMENTO',
	'CANCELADO',
	'COMPRA',
	'CORTESIA',
	'GARANTIA',
	'NAO_APROVADO',
	'NAO_RETIRA_3_MESES',
	'ORCAMENTO_APROVADO',
	'RESOLVIDO',
	'SEM_DEFEITO'
	);
	ALTER TABLE tecnico MODIFY situacao ENUM('ATIVO','DESATIVADO','LICENCA');