ALTER TABLE Servico ADD forma_pagamento ENUM('DEBITO', 'CREDITO', 'PIX', 'BOLETO') DEFAULT NULL;
ALTER TABLE Servico ADD valor_pecas double DEFAULT NULL;
ALTER TABLE Servico ADD valor_comissao double DEFAULT NULL;
ALTER TABLE Servico ADD data_pagamento_comissao datetime(6) DEFAULT NULL;