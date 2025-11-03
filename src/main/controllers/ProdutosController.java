package main.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.database.FornecedorDAO;
import main.database.ProdutoDAO;
import main.models.Fornecedor;
import main.models.Produto;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ProdutosController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Produto> tableProdutos;

    @FXML
    private TableColumn<Produto, Integer> colId;

    @FXML
    private TableColumn<Produto, String> colCodigo;

    @FXML
    private TableColumn<Produto, String> colNome;

    @FXML
    private TableColumn<Produto, String> colCategoria;

    @FXML
    private TableColumn<Produto, BigDecimal> colPreco;

    @FXML
    private TableColumn<Produto, Integer> colEstoque;

    @FXML
    private TableColumn<Produto, String> colFornecedor;

    @FXML
    private Button btnNovo;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnExcluir;

    @FXML
    private Label lblTotal;

    @FXML
    private ScrollPane formContainer;

    @FXML
    private Text lblFormTitle;

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtNome;

    @FXML
    private ComboBox<String> cbCategoria;

    @FXML
    private TextField txtPreco;

    @FXML
    private TextField txtEstoque;

    @FXML
    private ComboBox<Fornecedor> cbFornecedor;

    @FXML
    private TextArea txtDescricao;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnCancelar;

    private ProdutoDAO produtoDAO;
    private FornecedorDAO fornecedorDAO;
    private ObservableList<Produto> produtos;
    private ObservableList<Fornecedor> fornecedores;
    private Produto produtoSelecionado;
    private boolean modoEdicao = false;

    @FXML
    void initialize() {
        produtoDAO = new ProdutoDAO();
        fornecedorDAO = new FornecedorDAO();

        // Configurar TableView
        configurarTableView();

        // Configurar ComboBoxes
        configurarComboBoxes();

        // Carregar dados
        carregarProdutos();
        carregarFornecedores();
        atualizarTotal();

        // Configurar busca
        configurarBusca();

        // Ocultar formulário inicialmente
        formContainer.setVisible(false);
        formContainer.setManaged(false);
    }

    private void configurarTableView() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idProduto"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoBarras"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("unidadeMedida"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("precoVenda"));
        colEstoque.setCellValueFactory(new PropertyValueFactory<>("estoqueAtual"));
        colFornecedor.setCellValueFactory(cellData -> {
            Integer idFornecedor = cellData.getValue().getIdFornecedor();
            if (idFornecedor != null) {
                Fornecedor fornecedor = fornecedores.stream()
                    .filter(f -> f.getIdFornecedor() == idFornecedor)
                    .findFirst()
                    .orElse(null);
                return new javafx.beans.property.SimpleStringProperty(
                    fornecedor != null ? fornecedor.getRazaoSocial() : "");
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });

        // Configurar formatação de preço
        colPreco.setCellFactory(column -> new TableCell<Produto, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("R$ %.2f", item));
                }
            }
        });

        produtos = FXCollections.observableArrayList();
        tableProdutos.setItems(produtos);
    }

    private void configurarComboBoxes() {
        // Categorias/Unidades de medida
        cbCategoria.setItems(FXCollections.observableArrayList(
            "UN", "KG", "L", "M", "M²", "M³", "PCT", "CX", "FD"
        ));

        fornecedores = FXCollections.observableArrayList();
        cbFornecedor.setItems(fornecedores);
    }

    private void configurarBusca() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarProdutos(newValue);
        });
    }

    private void carregarProdutos() {
        try {
            List<Produto> listaProdutos = produtoDAO.buscarTodos();
            produtos.clear();
            produtos.addAll(listaProdutos);
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar produtos", e.getMessage());
        }
    }

    private void carregarFornecedores() {
        try {
            List<Fornecedor> listaFornecedores = fornecedorDAO.buscarTodos();
            fornecedores.clear();
            fornecedores.addAll(listaFornecedores);
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar fornecedores", e.getMessage());
        }
    }

    private void atualizarTotal() {
        try {
            int total = produtoDAO.contarTotal();
            lblTotal.setText("Total: " + total + " produto(s)");
        } catch (SQLException e) {
            lblTotal.setText("Total: Erro ao contar");
        }
    }

    private void filtrarProdutos(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            carregarProdutos();
        } else {
            try {
                List<Produto> listaFiltrada = produtoDAO.buscarPorDescricao(filtro.trim());
                produtos.clear();
                produtos.addAll(listaFiltrada);
            } catch (SQLException e) {
                mostrarErro("Erro ao filtrar produtos", e.getMessage());
            }
        }
    }

    @FXML
    void handleNovo() {
        modoEdicao = false;
        produtoSelecionado = null;
        limparFormulario();
        lblFormTitle.setText("Novo Produto");
        mostrarFormulario(true);
    }

    @FXML
    void handleEditar() {
        produtoSelecionado = tableProdutos.getSelectionModel().getSelectedItem();
        if (produtoSelecionado == null) {
            mostrarAlerta("Seleção necessária", "Selecione um produto para editar.");
            return;
        }

        modoEdicao = true;
        preencherFormulario(produtoSelecionado);
        lblFormTitle.setText("Editar Produto");
        mostrarFormulario(true);
    }

    @FXML
    void handleExcluir() {
        produtoSelecionado = tableProdutos.getSelectionModel().getSelectedItem();
        if (produtoSelecionado == null) {
            mostrarAlerta("Seleção necessária", "Selecione um produto para excluir.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar exclusão");
        confirmacao.setHeaderText("Deseja realmente excluir este produto?");
        confirmacao.setContentText("Produto: " + produtoSelecionado.getDescricao());

        if (confirmacao.showAndWait().get() == ButtonType.OK) {
            try {
                if (produtoDAO.excluir(produtoSelecionado.getIdProduto())) {
                    carregarProdutos();
                    atualizarTotal();
                    mostrarSucesso("Produto excluído com sucesso!");
                } else {
                    mostrarErro("Erro", "Não foi possível excluir o produto.");
                }
            } catch (SQLException e) {
                mostrarErro("Erro ao excluir produto", e.getMessage());
            }
        }
    }

    @FXML
    void handleSalvar() {
        if (!validarFormulario()) {
            return;
        }

        try {
            Produto produto = criarProdutoDoFormulario();

            boolean sucesso;
            if (modoEdicao) {
                sucesso = produtoDAO.atualizar(produto);
            } else {
                sucesso = produtoDAO.inserir(produto);
            }

            if (sucesso) {
                carregarProdutos();
                atualizarTotal();
                mostrarFormulario(false);
                mostrarSucesso(modoEdicao ? "Produto atualizado com sucesso!" : "Produto cadastrado com sucesso!");
            } else {
                mostrarErro("Erro", "Não foi possível salvar o produto.");
            }
        } catch (SQLException e) {
            mostrarErro("Erro ao salvar produto", e.getMessage());
        }
    }

    @FXML
    void handleCancelar() {
        mostrarFormulario(false);
    }

    private void mostrarFormulario(boolean mostrar) {
        formContainer.setVisible(mostrar);
        formContainer.setManaged(mostrar);
        tableProdutos.setVisible(!mostrar);
        tableProdutos.setManaged(!mostrar);
    }

    private void limparFormulario() {
        txtCodigo.clear();
        txtNome.clear();
        cbCategoria.setValue(null);
        txtPreco.clear();
        txtEstoque.clear();
        cbFornecedor.setValue(null);
        txtDescricao.clear();
    }

    private void preencherFormulario(Produto produto) {
        txtCodigo.setText(produto.getCodigoBarras());
        txtNome.setText(produto.getDescricao());
        cbCategoria.setValue(produto.getUnidadeMedida());
        txtPreco.setText(produto.getPrecoVenda() != null ? produto.getPrecoVenda().toString() : "");
        txtEstoque.setText(String.valueOf(produto.getEstoqueAtual()));

        if (produto.getIdFornecedor() != null) {
            Fornecedor fornecedor = fornecedores.stream()
                .filter(f -> f.getIdFornecedor() == produto.getIdFornecedor())
                .findFirst()
                .orElse(null);
            cbFornecedor.setValue(fornecedor);
        } else {
            cbFornecedor.setValue(null);
        }

        txtDescricao.setText(produto.getDescricao());
    }

    private Produto criarProdutoDoFormulario() {
        Produto produto = modoEdicao ? produtoSelecionado : new Produto();

        if (!modoEdicao) {
            produto.setIdEmpresa(1); // ID da empresa padrão
        }

        produto.setDescricao(txtNome.getText().trim());
        produto.setCodigoBarras(txtCodigo.getText().trim().isEmpty() ? null : txtCodigo.getText().trim());
        produto.setUnidadeMedida(cbCategoria.getValue());
        produto.setPrecoVenda(new BigDecimal(txtPreco.getText().replace(",", ".")));
        produto.setEstoqueAtual(Integer.parseInt(txtEstoque.getText()));

        Fornecedor fornecedorSelecionado = cbFornecedor.getValue();
        produto.setIdFornecedor(fornecedorSelecionado != null ? fornecedorSelecionado.getIdFornecedor() : null);

        return produto;
    }

    private boolean validarFormulario() {
        StringBuilder erros = new StringBuilder();

        if (txtNome.getText().trim().isEmpty()) {
            erros.append("- Nome do produto é obrigatório\n");
        }

        if (!txtPreco.getText().trim().isEmpty()) {
            try {
                new BigDecimal(txtPreco.getText().replace(",", "."));
            } catch (NumberFormatException e) {
                erros.append("- Preço deve ser um número válido\n");
            }
        }

        if (!txtEstoque.getText().trim().isEmpty()) {
            try {
                Integer.parseInt(txtEstoque.getText());
            } catch (NumberFormatException e) {
                erros.append("- Estoque deve ser um número inteiro\n");
            }
        }

        // Verificar código de barras único
        if (!txtCodigo.getText().trim().isEmpty()) {
            try {
                Integer idExcluir = modoEdicao ? produtoSelecionado.getIdProduto() : null;
                if (produtoDAO.codigoBarrasExiste(txtCodigo.getText().trim(), idExcluir)) {
                    erros.append("- Código de barras já existe\n");
                }
            } catch (SQLException e) {
                erros.append("- Erro ao verificar código de barras\n");
            }
        }

        if (erros.length() > 0) {
            mostrarErro("Dados inválidos", erros.toString());
            return false;
        }

        return true;
    }

    private void mostrarErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
