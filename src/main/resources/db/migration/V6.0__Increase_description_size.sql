ALTER TABLE Servico MODIFY descricao VARCHAR(10240);
ALTER TABLE Servico MODIFY situacao ENUM(
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
'RESOLVIDO' ,
'SEM_DEFEITO');