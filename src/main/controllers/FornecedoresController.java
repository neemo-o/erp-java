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
import main.models.Fornecedor;

import java.sql.SQLException;
import java.util.List;

public class FornecedoresController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Fornecedor> tableFornecedores;

    @FXML
    private TableColumn<Fornecedor, Integer> colId;

    @FXML
    private TableColumn<Fornecedor, String> colNome;

    @FXML
    private TableColumn<Fornecedor, String> colCnpj;

    @FXML
    private TableColumn<Fornecedor, String> colContato;

    @FXML
    private TableColumn<Fornecedor, String> colTelefone;

    @FXML
    private TableColumn<Fornecedor, String> colEmail;

    @FXML
    private TableColumn<Fornecedor, String> colEndereco;

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

    private FornecedorDAO fornecedorDAO;
    private ObservableList<Fornecedor> fornecedores;
    private Fornecedor fornecedorSelecionado;
    private boolean modoEdicao = false;

    @FXML
    void initialize() {
        fornecedorDAO = new FornecedorDAO();

        // Configurar TableView
        configurarTableView();

        // Carregar dados
        carregarFornecedores();
        atualizarTotal();

        // Configurar busca
        configurarBusca();

        // Ocultar formulário inicialmente
        formContainer.setVisible(false);
        formContainer.setManaged(false);
    }

    private void configurarTableView() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idFornecedor"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("razaoSocial"));
        colCnpj.setCellValueFactory(new PropertyValueFactory<>("cnpj"));
        colContato.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEndereco.setCellValueFactory(cellData -> {
            Fornecedor fornecedor = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(fornecedor.getEnderecoCompleto());
        });

        fornecedores = FXCollections.observableArrayList();
        tableFornecedores.setItems(fornecedores);
    }

    private void configurarBusca() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarFornecedores(newValue);
        });
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
            int total = fornecedorDAO.contarTotal();
            lblTotal.setText("Total: " + total + " fornecedor(es)");
        } catch (SQLException e) {
            lblTotal.setText("Total: Erro ao contar");
        }
    }

    private void filtrarFornecedores(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            carregarFornecedores();
        } else {
            try {
                List<Fornecedor> listaFiltrada = fornecedorDAO.buscarPorRazaoSocial(filtro.trim());
                fornecedores.clear();
                fornecedores.addAll(listaFiltrada);
            } catch (SQLException e) {
                mostrarErro("Erro ao filtrar fornecedores", e.getMessage());
            }
        }
    }

    @FXML
    void handleNovo() {
        modoEdicao = false;
        fornecedorSelecionado = null;
        limparFormulario();
        lblFormTitle.setText("Novo Fornecedor");
        mostrarFormulario(true);
    }

    @FXML
    void handleEditar() {
        fornecedorSelecionado = tableFornecedores.getSelectionModel().getSelectedItem();
        if (fornecedorSelecionado == null) {
            mostrarAlerta("Seleção necessária", "Selecione um fornecedor para editar.");
            return;
        }

        modoEdicao = true;
        preencherFormulario(fornecedorSelecionado);
        lblFormTitle.setText("Editar Fornecedor");
        mostrarFormulario(true);
    }

    @FXML
    void handleExcluir() {
        fornecedorSelecionado = tableFornecedores.getSelectionModel().getSelectedItem();
        if (fornecedorSelecionado == null) {
            mostrarAlerta("Seleção necessária", "Selecione um fornecedor para excluir.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar exclusão");
        confirmacao.setHeaderText("Deseja realmente excluir este fornecedor?");
        confirmacao.setContentText("Fornecedor: " + fornecedorSelecionado.getRazaoSocial());

        if (confirmacao.showAndWait().get() == ButtonType.OK) {
            try {
                if (fornecedorDAO.excluir(fornecedorSelecionado.getIdFornecedor())) {
                    carregarFornecedores();
                    atualizarTotal();
                    mostrarSucesso("Fornecedor excluído com sucesso!");
                } else {
                    mostrarErro("Erro", "Não foi possível excluir o fornecedor.");
                }
            } catch (SQLException e) {
                mostrarErro("Erro ao excluir fornecedor", e.getMessage());
            }
        }
    }

    @FXML
    void handleSalvar() {
        if (!validarFormulario()) {
            return;
        }

        try {
            Fornecedor fornecedor = criarFornecedorDoFormulario();

            boolean sucesso;
            if (modoEdicao) {
                sucesso = fornecedorDAO.atualizar(fornecedor);
            } else {
                sucesso = fornecedorDAO.inserir(fornecedor);
            }

            if (sucesso) {
                carregarFornecedores();
                atualizarTotal();
                mostrarFormulario(false);
                mostrarSucesso(modoEdicao ? "Fornecedor atualizado com sucesso!" : "Fornecedor cadastrado com sucesso!");
            } else {
                mostrarErro("Erro", "Não foi possível salvar o fornecedor.");
            }
        } catch (SQLException e) {
            mostrarErro("Erro ao salvar fornecedor", e.getMessage());
        }
    }

    @FXML
    void handleCancelar() {
        mostrarFormulario(false);
    }

    private void mostrarFormulario(boolean mostrar) {
        formContainer.setVisible(mostrar);
        formContainer.setManaged(mostrar);
        tableFornecedores.setVisible(!mostrar);
        tableFornecedores.setManaged(!mostrar);
    }

    private void limparFormulario() {
        txtNome.clear();
        txtCnpj.clear();
        txtContato.clear();
        txtTelefone.clear();
        txtEmail.clear();
        txtEndereco.clear();
    }

    private void preencherFormulario(Fornecedor fornecedor) {
        txtNome.setText(fornecedor.getRazaoSocial());
        txtCnpj.setText(fornecedor.getCnpj());
        txtContato.setText(fornecedor.getTelefone());
        txtTelefone.setText(fornecedor.getTelefone());
        txtEmail.setText(fornecedor.getEmail());
        txtEndereco.setText(fornecedor.getEnderecoCompleto());
    }

    private Fornecedor criarFornecedorDoFormulario() {
        Fornecedor fornecedor = modoEdicao ? fornecedorSelecionado : new Fornecedor();

        if (!modoEdicao) {
            fornecedor.setIdEmpresa(1); // ID da empresa padrão
        }

        fornecedor.setRazaoSocial(txtNome.getText().trim());
        fornecedor.setCnpj(txtCnpj.getText().trim());
        fornecedor.setTelefone(txtTelefone.getText().trim());
        fornecedor.setEmail(txtEmail.getText().trim().isEmpty() ? null : txtEmail.getText().trim());

        // Por enquanto, não salvar endereço separado já que o FXML tem apenas um campo único
        // Em uma versão futura, pode-se implementar parsing do endereço único

        return fornecedor;
    }

    private boolean validarFormulario() {
        StringBuilder erros = new StringBuilder();

        if (txtNome.getText().trim().isEmpty()) {
            erros.append("- Razão social é obrigatória\n");
        }

        if (txtCnpj.getText().trim().isEmpty()) {
            erros.append("- CNPJ é obrigatório\n");
        } else {
            // Validação básica de CNPJ (14 dígitos)
            String cnpj = txtCnpj.getText().trim().replaceAll("[^0-9]", "");
            if (cnpj.length() != 14) {
                erros.append("- CNPJ deve ter 14 dígitos\n");
            } else {
                try {
                    Integer idExcluir = modoEdicao ? fornecedorSelecionado.getIdFornecedor() : null;
                    if (fornecedorDAO.cnpjExiste(cnpj, idExcluir)) {
                        erros.append("- CNPJ já cadastrado\n");
                    }
                } catch (SQLException e) {
                    erros.append("- Erro ao verificar CNPJ\n");
                }
            }
        }

        if (!txtEmail.getText().trim().isEmpty()) {
            // Validação básica de email
            String email = txtEmail.getText().trim();
            if (!email.contains("@") || !email.contains(".")) {
                erros.append("- Email inválido\n");
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
