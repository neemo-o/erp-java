package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ProdutosController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<?> tableProdutos;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colCodigo;

    @FXML
    private TableColumn<?, ?> colNome;

    @FXML
    private TableColumn<?, ?> colCategoria;

    @FXML
    private TableColumn<?, ?> colPreco;

    @FXML
    private TableColumn<?, ?> colEstoque;

    @FXML
    private TableColumn<?, ?> colFornecedor;

    @FXML
    private Button btnNovo;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnExcluir;

    @FXML
    private Label lblTotal;

    @FXML
    private VBox formContainer;

    @FXML
    private Text lblFormTitle;

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtNome;

    @FXML
    private ComboBox<?> cbCategoria;

    @FXML
    private TextField txtPreco;

    @FXML
    private TextField txtEstoque;

    @FXML
    private ComboBox<?> cbFornecedor;

    @FXML
    private TextArea txtDescricao;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnCancelar;

    @FXML
    void initialize() {
        // Código de inicialização aqui
    }

    @FXML
    void handleNovo() {
        // Implementar lógica de novo produto
    }

    @FXML
    void handleEditar() {
        // Implementar lógica de editar produto
    }

    @FXML
    void handleExcluir() {
        // Implementar lógica de excluir produto
    }

    @FXML
    void handleSalvar() {
        // Implementar lógica de salvar produto
    }

    @FXML
    void handleCancelar() {
        // Implementar lógica de cancelar
    }

}
