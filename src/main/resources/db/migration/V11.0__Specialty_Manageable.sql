-- Makes specialty a managed, ordered catalog:
--   * adds soft-delete (Ativo) + timestamps + unique name
--   * normalizes ids so 'Outros' (generic, always present) is id 1 and known
--     specialties follow the canonical order; works even on drifted databases

-- 1. Remove duplicates keeping the lowest id (defensive; unique key added next)
DELETE e1
FROM especialidade e1
INNER JOIN especialidade e2
    ON e1.Conhecimento = e2.Conhecimento
    AND e1.Id > e2.Id;

-- 2. Manageable columns + unique name
ALTER TABLE especialidade
    ADD COLUMN Ativo TINYINT(1) NOT NULL DEFAULT 1,
    ADD COLUMN Criado_Em DATETIME NULL,
    ADD COLUMN Atualizado_Em DATETIME NULL,
    ADD UNIQUE KEY uk_especialidade_conhecimento (Conhecimento);

-- 3. Guarantee generic rows exist before normalization
INSERT IGNORE INTO especialidade (Conhecimento, Ativo, Criado_Em, Atualizado_Em)
VALUES ('Outros', 1, NOW(), NOW());
INSERT IGNORE INTO especialidade (Conhecimento, Ativo, Criado_Em, Atualizado_Em)
VALUES ('Air Fryer', 1, NOW(), NOW());

-- 4. Build canonical mapping: 'Outros' first, then known specialties, extras after
CREATE TEMPORARY TABLE esp_remap (
    old_id INT NOT NULL,
    new_id INT NOT NULL,
    PRIMARY KEY (old_id)
) ENGINE=InnoDB;

INSERT INTO esp_remap (old_id, new_id)
SELECT Id, rn
FROM (
    SELECT Id,
           ROW_NUMBER() OVER (
               ORDER BY CASE Conhecimento
                   WHEN 'Outros' THEN 1
                   WHEN 'Adega' THEN 2
                   WHEN 'Bebedouro' THEN 3
                   WHEN 'Climatizador' THEN 4
                   WHEN 'Cooler' THEN 5
                   WHEN 'Frigobar' THEN 6
                   WHEN 'Geladeira' THEN 7
                   WHEN 'Lava Louça' THEN 8
                   WHEN 'Lava Roupa' THEN 9
                   WHEN 'Microondas' THEN 10
                   WHEN 'Purificador' THEN 11
                   WHEN 'Secadora' THEN 12
                   WHEN 'Air Fryer' THEN 13
                   ELSE 14
               END,
               Id
           ) AS rn
    FROM especialidade
) ranked;

-- 5. Remap ids (FK-safe two-phase: park current ids, remap, restore checks)
SET FOREIGN_KEY_CHECKS = 0;

UPDATE especialidade SET Id = Id + 10000;

UPDATE tecnico_especialidade te
JOIN esp_remap m ON te.id_especialidade = m.old_id
SET te.id_especialidade = te.id_especialidade + 10000;

UPDATE tecnico_especialidade te
JOIN esp_remap m ON te.id_especialidade = m.old_id + 10000
SET te.id_especialidade = m.new_id;

UPDATE especialidade e
JOIN esp_remap m ON e.Id = m.old_id + 10000
SET e.Id = m.new_id;

SET FOREIGN_KEY_CHECKS = 1;

DROP TEMPORARY TABLE esp_remap;

-- 6. Timestamps for legacy rows + guarantee 'Outros' always active
UPDATE especialidade
SET Criado_Em = COALESCE(Criado_Em, NOW()),
    Atualizado_Em = COALESCE(Atualizado_Em, NOW())
WHERE Criado_Em IS NULL;

UPDATE especialidade SET Ativo = 1 WHERE Conhecimento = 'Outros';

-- 7. Next auto increment above the canonical catalog
ALTER TABLE especialidade AUTO_INCREMENT = 14;
