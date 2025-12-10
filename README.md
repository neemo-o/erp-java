# Sistema Integrado de Supermercados

Sistema integrado desenvolvido em Java para gestão completa de operações de supermercados, incluindo controle de estoque, vendas, compras, fornecedores, clientes e geração de relatórios.

## Badges

[![Java Version](https://img.shields.io/badge/Java-21-orange)](https://www.oracle.com/java/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)](https://www.postgresql.org/)
[![JavaFX](https://img.shields.io/badge/JavaFX-21-green)](https://openjfx.io/)

## Funcionalidades Principais

- **Autenticação de Usuários**: Sistema de login para controle de acesso, com tipos de usuário (admin/user).
- **Gestão de Clientes**: Cadastro e manutenção de dados de clientes (CPF, nome, endereço, contato).
- **Gestão de Fornecedores**: Registro de fornecedores (CNPJ, razão social, endereço), associados a produtos.
- **Produtos e Estoque**: Controle de produtos (descrição, código de barras, unidades, preços, estoque), movimentações e ajustes.
- **Vendas**: Registro de vendas com itens, valores totais e formas de pagamento (PIX, Cartão, Dinheiro).
- **Compras**: Controle de compras de fornecedores, com itens e valores.
- **Relatórios**: Geração de relatórios de vendas, compras e estoque.
- **Dashboard**: Visão geral com métricas de vendas, produtos e clientes cadastrados.
- **Configurações**: Ajustes gerais do sistema.


## Tecnologias Utilizadas

- **Linguagem**: Java 21
- **Interface Gráfica**: JavaFX (para aplicações desktop)
- **Banco de Dados**: PostgreSQL 15+
- **Conectividade**: JDBC (PostgreSQL Driver)
- **Estrutura**: MVC (Models, Views, Controllers) com FXML para layouts

Bibliotecas incluídas em `lib/`: JavaFX modules, PostgreSQL JDBC driver.

## Arquitetura / Estrutura do Projeto

O projeto segue uma arquitetura MVC (Model-View-Controller) com camadas bem definidas:

### Pastas Importantes
- `src/main/`: Código fonte principal
  - `Main.java`: Ponto de entrada da aplicação (inicia com tela de login).
  - `controllers/`: Lógica de controle (ex: MainScreenController, ProdutosController) - gerenciam eventos e integração com banco.
  - `models/`: Modelos de dados (ex: Cliente.java, Produto.java) - representam tabelas do banco.
  - `database/`: Acesso a dados (DAO classes como ProdutoDAO.java) e scripts SQL.
  - `view/`: Interfaces FXML (ex: Produtos.fxml, MainScreen.fxml) - definições das telas.
  - `resources/`: Recursos estáticos (ícones SVG, imagens).
  - `styles/`: Folhas de estilo CSS para customização da interface.
- `lib/`: Dependências JAR externas (JavaFX, PostgreSQL driver).
- `src/main/database/scripts/`: Scripts SQL para criação do banco de dados.

### Módulos
1. **Autenticação**: Login e registro de usuários (base `erp_licencas`).
2. **Dashboard**: Painel principal com métricas em tempo real.
3. **Produtos**: Cadastro, edição e consulta de produtos com controle de estoque.
4. **Clientes**: Gerenciamento de dados de clientes pessoa física (CPF).
5. **Vendas**: Processamento de vendas e itens vendidos.
6. **Estoque**: Movimentações (entrada/saída/ajuste) e controle de inventário.
7. **Fornecedores**: Cadastro de fornecedores e associação com produtos.
8. **Relatórios**: Geração de relatórios financeiros e operacionais (a implementar).
9. **Configurações**: Ajustes do sistema.

O sistema utiliza duas bases de dados PostgreSQL: `erp_licencas` para autenticação e `erp_oficial` para dados operacionais.

## Instalação e Configuração

### Pré-requisitos

- **Java 21** instalado (JDK - Java Development Kit).
- **PostgreSQL 15+** rodando localmente (porta 5432 padrão).
- **Git** para clonar o repositório.

### Como Instalar

1. Clone o repositório:
   ```bash
   git clone https://github.com/neemo-o/erp-java.git
   cd erp-java
   ```

2. Crie os bancos de dados no PostgreSQL:
   - Acesse o psql ou pgAdmin.
   - Execute os scripts SQL em `src/main/database/scripts/`:
     - Primeiro, crie os bancos:
       ```sql
       CREATE DATABASE erp_licencas;
       CREATE DATABASE erp_oficial;
       ```
     - Execute o script `erp-oficial.sql` no banco `erp_oficial` para criar tabelas e índices.
     - (Opcional: Outros scripts para ajustes específicos).

3. Compile o projeto (usando JavaFX libs em `lib/` - já incluído):
   - Se usando IDE (VS Code): Abra o projeto e configure classpath com JARs em `lib/`.
   - Comando manual (JavaFX module-path):
     ```bash
     javac -cp "lib/*" -d out src/main/**/*.java
     ```

### Como Rodar

1. Execute a aplicação:
   - Se via IDE: Execute `Main.java`.
   - Comando manual:
     ```bash
     java -cp "lib/*;out" --module-path lib --add-modules javafx.controls,javafx.fxml main.Main
     ```

2. Login: Use credenciais padrão (se inserido no script): usuário "User 1", senha "123456" (tipo admin).

### Variáveis de Ambiente

Configurações de banco hard-coded em `Data_Config.java`:
- Host: localhost:5432
- Usuário/Senha: postgres / postgres (padrão)
- Bases: erp_licencas e erp_oficial

Para produção, edite `Data_Config.java` ou mova para variáveis de ambiente (ex: System properties ou arquivo externo).

## Como Usar

1. **Login**: Abra o app, insira login/senha na tela inicial.
2. **Dashboard**: Após login, visualize métricas gerais (vendas totais, total produtos, total clientes).
3. **Navegação**: Use o menu lateral para acessar módulos:
   - Produtos: Adicione/edite produtos com dados como preço custo/venda, fornecedor, estoque.
   - Clientes: Cadastre clientes com CPF, nome, endereço.
   - Vendas: Inicie venda, adicione itens, finalize com forma de pagamento.
   - Estoque: Registre movimentações (entrada via compra, saída via venda).
   - Fornecedores: Cadastre fornecedores e associe a produtos.
   - Relatórios: Exporte dados de vendas/compras (em desenvolvimento).
4. **Configurações**: Ajuste preferências do sistema.
5. **Logout**: Use as opções de minimizar/fechar janela.

O sistema valida dados (ex: formato CEP, valores positivos) e integram em tempo real com o DB.

## API

Este é um aplicativo desktop JavaFX; não expõe APIs REST. Toda interação é via interface gráfica.

## Banco de Dados

### Tabelas Principais

- **enderecos**: Endereços compartilhados (logradouro, número, CEP brasileiro).
- **fornecedor**: Fornecedores (CNPJ, razão social, telefone, e-mail, endereço).
- **produto**: Produtos (descrição, código barras, unidade, preços, estoque, fornecedor).
- **clientes**: Clientes pessoa física (CPF, nome, telefone, endereço, status pago/não pago).
- **venda**: Vendas (data, valor total, forma pagamento).
- **item_venda**: Itens de vendas (venda, produto, quantidade, preço unitário).
- **compra**: Compras de fornecedores (fornecedor, data, valor, forma pagamento).
- **item_compra**: Itens de compras.
- **movimentacao_estoque**: Movimentações (entrada/saída/ajuste) para rastreamento de estoque.
- **licencas**: Usuários do sistema (nome, senha, tipo: admin/user).

### Relacionamentos

- Produto → Fornecedor (FK: id_fornecedor)
- Item_Venda → Venda e Produto (FKs)
- Item_Compra → Compra e Produto (FKs)
- Cliente/Fornecedor → Endereco (FK: id_endereco)
- Movimentacao_Estoque → Produto (FK)

Índices otimizados em FKs e campos frequentes (ex: código barras, datas). Triggers auto-atualizam datas de modificação.

## Contribuição

1. Fork o projeto.
2. Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`).
3. Commit suas mudanças (`git commit -m 'Adiciona nova funcionalidade'`).
4. Push para branch (`git push origin feature/nova-funcionalidade`).
5. Abra um Pull Request.

Padrões: Use camelCase para variáveis, comentários em português, testes para novas funcionalidades.

## Licença

Este projeto está licenciado sob a MIT License - veja o arquivo [LICENSE](LICENSE) para detalhes.

## Autor / Contato

**Desenvolvedores**: 
Emanuel Gomes,
Gustavo Henrique,
Gustavo Ramon,
Marco Antônio e
Neemias Vasconcelos.

**GitHub**: [neemo-o/erp-java](https://github.com/neemo-o/erp-java)

Para questões ou sugestões, abra uma issue no repositório.
