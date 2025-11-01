-- Active: 1761779476832@@127.0.0.1@5432@erp_lincences

--Vou Separar as credenciais para uma tabela fora do licences para não ficar atrelado diretamente ao cnpj e sim ao usuario.
CREATE TABLE IF NOT EXISTS licenca_credenciais (
    id SERIAL PRIMARY KEY,
    cnpj VARCHAR(18) NOT NULL,
    credenciais VARCHAR(50) NOT NULL,
    senha VARCHAR(50) NOT NULL,
    tipo_usuario VARCHAR(10) CHECK (tipo_usuario IN ('ADM','CAIXA')) NOT NULL,
    FOREIGN KEY (cnpj) REFERENCES licencas(cnpj)
);

-- FORNECEDOR?
