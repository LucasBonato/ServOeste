ALTER TABLE servico ADD forma_pagamento ENUM('DEBITO', 'DINHEIRO', 'CREDITO', 'PIX', 'BOLETO') DEFAULT NULL;
ALTER TABLE servico ADD valor_pecas double DEFAULT NULL;
ALTER TABLE servico ADD valor_comissao double DEFAULT NULL;
ALTER TABLE servico ADD data_pagamento_comissao datetime(6) DEFAULT NULL;