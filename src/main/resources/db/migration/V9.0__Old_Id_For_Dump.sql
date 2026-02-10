ALTER TABLE cliente ADD COLUMN old_id INT UNSIGNED UNIQUE;
ALTER TABLE tecnico ADD COLUMN old_id INT UNSIGNED UNIQUE;
ALTER TABLE servico ADD COLUMN old_id INT UNSIGNED UNIQUE;

ALTER TABLE tecnico_especialidade ADD UNIQUE KEY uk_tecnico_especialidade (id_especialidade, id_tecnico);