INSERT INTO Especialidade (conhecimento)
SELECT 'Adega' WHERE NOT EXISTS (SELECT 1 FROM Especialidade WHERE conhecimento = 'Adega');

INSERT INTO Especialidade (conhecimento)
SELECT 'Bebedouro' WHERE NOT EXISTS (SELECT 1 FROM Especialidade WHERE conhecimento = 'Bebedouro');

INSERT INTO especialidade (conhecimento)
SELECT 'Climatizador' WHERE NOT EXISTS (SELECT 1 FROM especialidade WHERE conhecimento = 'Climatizador');

INSERT INTO especialidade (conhecimento)
SELECT 'Cooler' WHERE NOT EXISTS (SELECT 1 FROM especialidade WHERE conhecimento = 'Cooler');

INSERT INTO especialidade (conhecimento)
SELECT 'Frigobar' WHERE NOT EXISTS (SELECT 1 FROM especialidade WHERE conhecimento = 'Frigobar');

INSERT INTO especialidade (conhecimento)
SELECT 'Geladeira' WHERE NOT EXISTS (SELECT 1 FROM especialidade WHERE conhecimento = 'Geladeira');

INSERT INTO especialidade (conhecimento)
SELECT 'Lava Louça' WHERE NOT EXISTS (SELECT 1 FROM especialidade WHERE conhecimento = 'Lava Louça');

INSERT INTO especialidade (conhecimento)
SELECT 'Lava Roupa' WHERE NOT EXISTS (SELECT 1 FROM especialidade WHERE conhecimento = 'Lava Roupa');

INSERT INTO especialidade (conhecimento)
SELECT 'Microondas' WHERE NOT EXISTS (SELECT 1 FROM especialidade WHERE conhecimento = 'Microondas');

INSERT INTO especialidade (conhecimento)
SELECT 'Purificador' WHERE NOT EXISTS (SELECT 1 FROM especialidade WHERE conhecimento = 'Purificador');

INSERT INTO especialidade (conhecimento)
SELECT 'Secadora' WHERE NOT EXISTS (SELECT 1 FROM especialidade WHERE conhecimento = 'Secadora');

INSERT INTO especialidade (conhecimento)
SELECT 'Outros' WHERE NOT EXISTS (SELECT 1 FROM especialidade WHERE conhecimento = 'Outros');