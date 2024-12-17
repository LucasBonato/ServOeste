CREATE TABLE cliente (
  id int PRIMARY KEY AUTO_INCREMENT,
  telefone_fixo varchar(10) DEFAULT NULL,
  telefone_celular varchar(11) DEFAULT NULL,
  nome varchar(50) NOT NULL,
  bairro varchar(255) DEFAULT NULL,
  endereco varchar(255) DEFAULT NULL,
  municipio varchar(255) DEFAULT NULL
);

CREATE TABLE especialidade (
  id int PRIMARY KEY AUTO_INCREMENT,
  conhecimento varchar(255) DEFAULT NULL
);

CREATE TABLE tecnico (
  id INT PRIMARY KEY AUTO_INCREMENT,
  telefone_fixo varchar(10) DEFAULT NULL,
  telefone_celular varchar(11) DEFAULT NULL,
  nome varchar(50) NOT NULL,
  sobrenome varchar(50) NOT NULL,
  situacao enum('ATIVO','DESATIVADO','LICENCA') DEFAULT NULL
);

CREATE TABLE servico (
  id INT PRIMARY KEY AUTO_INCREMENT,
  id_cliente int DEFAULT NULL,
  id_tecnico int DEFAULT NULL,
  situacao tinyint NOT NULL,
  valor double DEFAULT NULL,
  data_abertura datetime(6) NOT NULL,
  data_atendimento_efetiva datetime(6) DEFAULT NULL,
  data_atendimento_previsto datetime(6) DEFAULT NULL,
  data_fechamento datetime(6) DEFAULT NULL,
  data_fim_garantia datetime(6) DEFAULT NULL,
  data_inicio_garantia datetime(6) DEFAULT NULL,
  descricao varchar(255) NOT NULL,
  equipamento varchar(255) NOT NULL,
  filial varchar(255) NOT NULL,
  horario_previsto varchar(255) DEFAULT NULL,
  marca varchar(255) NOT NULL,
  CONSTRAINT servico_fky_tecnico FOREIGN KEY (id_tecnico) REFERENCES tecnico (id),
  CONSTRAINT servico_fky_cliente FOREIGN KEY (id_cliente) REFERENCES cliente (id),
  CONSTRAINT servico_chk_1 CHECK ((situacao between 0 and 13))
);

CREATE TABLE tecnico_especialidade (
  id_especialidade int NOT NULL,
  id_tecnico int NOT NULL,
  KEY tecnico_especialidade_fky_especialidade (id_especialidade),
  KEY tecnico_especialidade_fky_tecnico (id_tecnico),
  CONSTRAINT tecnico_especialidade_fky_especialidade FOREIGN KEY (id_especialidade) REFERENCES especialidade (id),
  CONSTRAINT tecnico_especialidade_fky_tecnico FOREIGN KEY (id_tecnico) REFERENCES tecnico (id)
);