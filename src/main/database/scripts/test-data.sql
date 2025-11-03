-- ========================================
-- DADOS DE TESTE PARA TODAS AS TABELAS
-- ========================================

-- Inserir endereços
INSERT INTO enderecos (logradouro, numero, complemento, bairro, cidade, estado, cep) VALUES
('Rua das Flores', '123', 'Apto 101', 'Centro', 'São Paulo', 'SP', '01234-567'),
('Av. Paulista', '456', NULL, 'Bela Vista', 'São Paulo', 'SP', '01310-100'),
('Rua XV de Novembro', '789', 'Sala 5', 'Centro', 'Rio de Janeiro', 'RJ', '20010-010'),
('Av. Boa Viagem', '101', NULL, 'Boa Viagem', 'Recife', 'PE', '51111-000'),
('Rua da Praia', '202', 'Casa', 'Praia Grande', 'Florianópolis', 'SC', '88010-000');

-- Inserir fornecedores
INSERT INTO fornecedor (cnpj, razao_social, senha_hash, telefone, e_mail, id_endereco) VALUES
('12.345.678/0001-90', 'Fornecedor A Ltda', 'hash123', '(11) 9999-9999', 'contato@fornecedorA.com', 1),
('98.765.432/0001-10', 'Distribuidora B S.A.', 'hash456', '(21) 8888-8888', 'vendas@distribuidoraB.com', 2),
('11.222.333/0001-44', 'Comércio C ME', 'hash789', '(81) 7777-7777', 'info@comercioC.com', 3);

-- Inserir produtos
INSERT INTO produto (descricao, codigo_barras, unidade_medida, preco_custo, preco_venda, estoque_atual, id_fornecedor) VALUES
('Produto 1', '789123456789', 'UN', 10.00, 15.00, 100, 1),
('Produto 2', '789123456790', 'KG', 20.00, 30.00, 50, 1),
('Produto 3', '789123456791', 'UN', 5.00, 8.00, 200, 2),
('Produto 4', '789123456792', 'LT', 15.00, 25.00, 75, 3);

-- Inserir movimentações de estoque
INSERT INTO movimentacao_estoque (id_produto, tipo, quantidade, origem) VALUES
(1, 'ENTRADA', 100, 'Compra inicial'),
(2, 'ENTRADA', 50, 'Compra inicial'),
(3, 'ENTRADA', 200, 'Compra inicial'),
(4, 'ENTRADA', 75, 'Compra inicial'),
(1, 'SAIDA', 10, 'Venda'),
(2, 'AJUSTE', 5, 'Ajuste de inventário');

-- Inserir vendas
INSERT INTO venda (valor_total, forma_pagamento) VALUES
(45.00, 'Cartão de Crédito'),
(30.00, 'Dinheiro'),
(80.00, 'PIX');

-- Inserir itens de venda
INSERT INTO item_venda (id_venda, id_produto, quantidade, preco_unitario) VALUES
(1, 1, 2, 15.00),
(1, 3, 1, 8.00),
(2, 2, 1, 30.00),
(3, 4, 2, 25.00),
(3, 1, 2, 15.00);

-- Inserir compras
INSERT INTO compra (id_fornecedor, valor_total, forma_pagamento) VALUES
(1, 1000.00, 'Boleto'),
(2, 500.00, 'Transferência'),
(3, 750.00, 'Cartão Empresarial');

-- Inserir itens de compra
INSERT INTO item_compra (id_compra, id_produto, quantidade, preco_unitario) VALUES
(1, 1, 100, 10.00),
(2, 3, 200, 5.00),
(3, 4, 75, 15.00),
(3, 2, 50, 20.00);

-- Inserir clientes
INSERT INTO clientes (cnpj, razao_social, nome_fantasia, inscricao_estadual, email_cliente, telefone_cliente, id_endereco_cliente, status_cliente) VALUES
('12345678000190', 'Cliente X Ltda', 'Cliente X', '123456789', 'contato@clientex.com', '(11) 5555-5555', 4, 'PAGO'),
('98765432000110', 'Empresa Y S.A.', 'Empresa Y', '987654321', 'vendas@empresay.com', '(21) 4444-4444', 5, 'NAO_PAGO'),
('11222333000144', 'Comércio Z ME', 'Comércio Z', '112223334', 'info@comercioz.com', '(81) 3333-3333', 1, 'PAGO');

-- Licenças já inseridas no schema principal
