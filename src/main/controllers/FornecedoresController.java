package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class FornecedoresController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<?> tableFornecedores;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colNome;

    @FXML
    private TableColumn<?, ?> colCnpj;

    @FXML
    private TableColumn<?, ?> colContato;

    @FXML
    private TableColumn<?, ?> colTelefone;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colEndereco;

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
    private TextField txtNome;

    @FXML
    private TextField txtCnpj;

    @FXML
    private TextField txtContato;

    @FXML
    private TextField txtTelefone;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtEndereco;

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
        // Implementar lógica de novo fornecedor
    }

    @FXML
    void handleEditar() {
        // Implementar lógica de editar fornecedor
    }

    @FXML
    void handleExcluir() {
        // Implementar lógica de excluir fornecedor
    }

    @FXML
    void handleSalvar() {
        // Implementar lógica de salvar fornecedor
    }

    @FXML
    void handleCancelar() {
        // Implementar lógica de cancelar
    }

}