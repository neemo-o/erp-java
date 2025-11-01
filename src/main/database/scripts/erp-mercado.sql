-- Active: 1761779476832@@127.0.0.1@5432@erp_oficial
-- ========================================
-- TABELA FORNECEDOR
-- ========================================
CREATE TABLE IF NOT EXISTS fornecedor (
    id_fornecedor SERIAL PRIMARY KEY,
    id_empresa INTEGER NOT NULL,
    cnpj VARCHAR(18) UNIQUE NOT NULL,
    razao_social VARCHAR(255) NOT NULL,
    senha_hash VARCHAR(255) NOT NULL,
    telefone VARCHAR(20),
    e_mail VARCHAR(255),
    rua VARCHAR(255),
    numero VARCHAR(10),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado VARCHAR(2),
    cep VARCHAR(10),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ========================================
-- TABELA PRODUTO
-- ========================================
CREATE TABLE IF NOT EXISTS produto (
    id_produto SERIAL PRIMARY KEY,
    id_empresa INTEGER NOT NULL,
    descricao TEXT NOT NULL,
    codigo_barras VARCHAR(50) UNIQUE,
    unidade_medida VARCHAR(10),
    preco_custo DECIMAL(10, 2),
    preco_venda DECIMAL(10, 2),
    estoque_atual INTEGER DEFAULT 0,
    id_fornecedor INTEGER,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_fornecedor) REFERENCES fornecedor(id_fornecedor) ON DELETE SET NULL
);

-- ========================================
-- TABELA MOVIMENTACAO_ESTOQUE
-- ========================================
CREATE TABLE IF NOT EXISTS movimentacao_estoque (
    id_movimento SERIAL PRIMARY KEY,
    id_empresa INTEGER NOT NULL,
    id_produto INTEGER NOT NULL,
    tipo VARCHAR(20) CHECK (tipo IN ('ENTRADA', 'SAIDA', 'AJUSTE')),
    quantidade INTEGER NOT NULL,
    data_movimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    origem TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_produto) REFERENCES produto(id_produto) ON DELETE CASCADE
);

-- ========================================
-- TABELA ITEM_VENDA
-- ========================================
CREATE TABLE IF NOT EXISTS item_venda (
    id_item SERIAL PRIMARY KEY,
    id_venda INTEGER NOT NULL,
    id_produto INTEGER NOT NULL,
    quantidade INTEGER NOT NULL,
    preco_unitario DECIMAL(10, 2) NOT NULL,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_produto) REFERENCES produto(id_produto) ON DELETE RESTRICT
);

-- ========================================
-- TABELA VENDA
-- ========================================
CREATE TABLE IF NOT EXISTS venda (
    id_venda SERIAL PRIMARY KEY,
    id_empresa INTEGER NOT NULL,
    data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    valor_total DECIMAL(10, 2) NOT NULL,
    forma_pagamento VARCHAR(50),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ========================================
-- TABELA COMPRA
-- ========================================
CREATE TABLE IF NOT EXISTS compra (
    id_compra SERIAL PRIMARY KEY,
    id_empresa INTEGER NOT NULL,
    id_fornecedor INTEGER NOT NULL,
    data_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    valor_total DECIMAL(10, 2) NOT NULL,
    forma_pagamento VARCHAR(50),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_fornecedor) REFERENCES fornecedor(id_fornecedor) ON DELETE RESTRICT
);

-- ========================================
-- TABELA ITEM_COMPRA
-- ========================================
CREATE TABLE IF NOT EXISTS item_compra (
    id_item SERIAL PRIMARY KEY,
    id_compra INTEGER NOT NULL,
    id_produto INTEGER NOT NULL,
    quantidade INTEGER NOT NULL,
    preco_unitario DECIMAL(10, 2) NOT NULL,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_compra) REFERENCES compra(id_compra) ON DELETE CASCADE,
    FOREIGN KEY (id_produto) REFERENCES produto(id_produto) ON DELETE RESTRICT
);

-- ========================================
-- ÍNDICES PARA MELHORAR PERFORMANCE
-- ========================================
CREATE INDEX idx_fornecedor_empresa ON fornecedor(id_empresa);
CREATE INDEX idx_fornecedor_cnpj ON fornecedor(cnpj);

CREATE INDEX idx_produto_empresa ON produto(id_empresa);
CREATE INDEX idx_produto_codigo_barras ON produto(codigo_barras);
CREATE INDEX idx_produto_fornecedor ON produto(id_fornecedor);

CREATE INDEX idx_movimentacao_empresa ON movimentacao_estoque(id_empresa);
CREATE INDEX idx_movimentacao_produto ON movimentacao_estoque(id_produto);
CREATE INDEX idx_movimentacao_data ON movimentacao_estoque(data_movimento);

CREATE INDEX idx_item_venda_venda ON item_venda(id_venda);
CREATE INDEX idx_item_venda_produto ON item_venda(id_produto);

CREATE INDEX idx_venda_empresa ON venda(id_empresa);
CREATE INDEX idx_venda_data ON venda(data_venda);

CREATE INDEX idx_compra_empresa ON compra(id_empresa);
CREATE INDEX idx_compra_fornecedor ON compra(id_fornecedor);
CREATE INDEX idx_compra_data ON compra(data_compra);

CREATE INDEX idx_item_compra_compra ON item_compra(id_compra);
CREATE INDEX idx_item_compra_produto ON item_compra(id_produto);

-- ========================================
-- TRIGGERS PARA ATUALIZAR DATA_ATUALIZACAO
-- ========================================
CREATE OR REPLACE FUNCTION atualizar_data_modificacao()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_atualizar_fornecedor
    BEFORE UPDATE ON fornecedor
    FOR EACH ROW
    EXECUTE FUNCTION atualizar_data_modificacao();

CREATE TRIGGER trigger_atualizar_produto
    BEFORE UPDATE ON produto
    FOR EACH ROW
    EXECUTE FUNCTION atualizar_data_modificacao();

-- ========================================
-- COMENTÁRIOS NAS TABELAS
-- ========================================
COMMENT ON TABLE fornecedor IS 'Cadastro de fornecedores';
COMMENT ON TABLE produto IS 'Cadastro de produtos';
COMMENT ON TABLE movimentacao_estoque IS 'Registro de movimentações de estoque';
COMMENT ON TABLE item_venda IS 'Itens de cada venda';
COMMENT ON TABLE venda IS 'Registro de vendas';
COMMENT ON TABLE compra IS 'Registro de compras';
COMMENT ON TABLE item_compra IS 'Itens de cada compra';