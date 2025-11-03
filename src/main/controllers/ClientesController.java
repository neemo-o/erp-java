package main.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.database.ClienteDAO;
import main.models.Cliente;

import java.sql.SQLException;
import java.util.List;

public class ClientesController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Cliente> tableClientes;

    @FXML
    private TableColumn<Cliente, String> colId;

    @FXML
    private TableColumn<Cliente, String> colNome;

    @FXML
    private TableColumn<Cliente, String> colCpfCnpj;

    @FXML
    private TableColumn<Cliente, String> colTelefone;

    @FXML
    private TableColumn<Cliente, String> colEmail;

    @FXML
    private TableColumn<Cliente, String> colEndereco;

    @FXML
    private TableColumn<Cliente, String> colStatus;

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
    private TextField txtCpfCnpj;

    @FXML
    private TextField txtInscricaoEstadual;

    @FXML
    private TextField txtTelefone;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtLogradouro;

    @FXML
    private TextField txtNumero;

    @FXML
    private TextField txtComplemento;

    @FXML
    private TextField txtBairro;

    @FXML
    private TextField txtCidade;

    @FXML
    private TextField txtEstado;

    @FXML
    private TextField txtCep;

    @FXML
    private ComboBox cbStatus;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnCancelar;

    private ClienteDAO clienteDAO;
    private ObservableList<Cliente> clientes;
    private Cliente clienteSelecionado;
    private boolean modoEdicao = false;

    @FXML
    void initialize() {
        clienteDAO = new ClienteDAO();

        // Configurar TableView
        configurarTableView();

        // Configurar ComboBox do status
        cbStatus.setItems(FXCollections.observableArrayList("PAGO", "NAO_PAGO"));

        // Carregar dados
        carregarClientes();
        atualizarTotal();

        // Configurar busca
        configurarBusca();

        // Ocultar formulário inicialmente
        formContainer.setVisible(false);
        formContainer.setManaged(false);
    }

    private void configurarTableView() {
        colId.setCellValueFactory(new PropertyValueFactory<>("cnpj"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("razaoSocial"));
        colCpfCnpj.setCellValueFactory(new PropertyValueFactory<>("cnpj"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefoneCliente"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("emailCliente"));

        // Configurar coluna de endereço para mostrar endereço completo
        colEndereco.setCellValueFactory(cellData -> {
            Cliente cliente = cellData.getValue();
            StringBuilder endereco = new StringBuilder();

            if (cliente.getLogradouro() != null && !cliente.getLogradouro().trim().isEmpty()) {
                endereco.append(cliente.getLogradouro());
            }
            if (cliente.getNumero() != null && !cliente.getNumero().trim().isEmpty()) {
                if (endereco.length() > 0) endereco.append(", ");
                endereco.append(cliente.getNumero());
            }
            if (cliente.getBairro() != null && !cliente.getBairro().trim().isEmpty()) {
                if (endereco.length() > 0) endereco.append(" - ");
                endereco.append(cliente.getBairro());
            }
            if (cliente.getCidade() != null && !cliente.getCidade().trim().isEmpty()) {
                if (endereco.length() > 0) endereco.append("/");
                endereco.append(cliente.getCidade());
            }

            return new javafx.beans.property.SimpleStringProperty(endereco.toString());
        });

        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusCliente"));

        clientes = FXCollections.observableArrayList();
        tableClientes.setItems(clientes);
    }

    private void configurarBusca() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarClientes(newValue);
        });
    }

    private void carregarClientes() {
        try {
            List<Cliente> listaClientes = clienteDAO.buscarTodos();
            clientes.clear();
            clientes.addAll(listaClientes);
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar clientes", e.getMessage());
        }
    }

    private void atualizarTotal() {
        try {
            int total = clienteDAO.contarTotal();
            lblTotal.setText("Total: " + total + " cliente(s)");
        } catch (SQLException e) {
            lblTotal.setText("Total: Erro ao contar");
        }
    }

    private void filtrarClientes(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            carregarClientes();
        } else {
            try {
                List<Cliente> listaFiltrada = clienteDAO.buscarPorRazaoSocial(filtro.trim());
                clientes.clear();
                clientes.addAll(listaFiltrada);
            } catch (SQLException e) {
                mostrarErro("Erro ao filtrar clientes", e.getMessage());
            }
        }
    }

    @FXML
    void handleNovo() {
        modoEdicao = false;
        clienteSelecionado = null;
        limparFormulario();
        lblFormTitle.setText("Novo Cliente");
        mostrarFormulario(true);
    }

    @FXML
    void handleEditar() {
        clienteSelecionado = tableClientes.getSelectionModel().getSelectedItem();
        if (clienteSelecionado == null) {
            mostrarAlerta("Seleção necessária", "Selecione um cliente para editar.");
            return;
        }

        modoEdicao = true;
        preencherFormulario(clienteSelecionado);
        lblFormTitle.setText("Editar Cliente");
        mostrarFormulario(true);
    }

    @FXML
    void handleExcluir() {
        clienteSelecionado = tableClientes.getSelectionModel().getSelectedItem();
        if (clienteSelecionado == null) {
            mostrarAlerta("Seleção necessária", "Selecione um cliente para excluir.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar exclusão");
        confirmacao.setHeaderText("Deseja realmente excluir este cliente?");
        confirmacao.setContentText("Cliente: " + clienteSelecionado.getRazaoSocial());

        if (confirmacao.showAndWait().get() == ButtonType.OK) {
            try {
                if (clienteDAO.excluir(clienteSelecionado.getCnpj())) {
                    carregarClientes();
                    atualizarTotal();
                    mostrarSucesso("Cliente excluído com sucesso!");
                } else {
                    mostrarErro("Erro", "Não foi possível excluir o cliente.");
                }
            } catch (SQLException e) {
                mostrarErro("Erro ao excluir cliente", e.getMessage());
            }
        }
    }

    @FXML
    void handleSalvar() {
        if (!validarFormulario()) {
            return;
        }

        try {
            Cliente cliente = criarClienteDoFormulario();

            boolean sucesso;
            if (modoEdicao) {
                sucesso = clienteDAO.atualizar(cliente);
            } else {
                sucesso = clienteDAO.inserir(cliente);
            }

            if (sucesso) {
                carregarClientes();
                atualizarTotal();
                mostrarFormulario(false);
                mostrarSucesso(modoEdicao ? "Cliente atualizado com sucesso!" : "Cliente cadastrado com sucesso!");
            } else {
                mostrarErro("Erro", "Não foi possível salvar o cliente.");
            }
        } catch (SQLException e) {
            mostrarErro("Erro ao salvar cliente", e.getMessage());
        }
    }

    @FXML
    void handleCancelar() {
        mostrarFormulario(false);
    }

    private void mostrarFormulario(boolean mostrar) {
        formContainer.setVisible(mostrar);
        formContainer.setManaged(mostrar);
        tableClientes.setVisible(!mostrar);
        tableClientes.setManaged(!mostrar);
    }

    private void limparFormulario() {
        txtNome.clear();
        txtCpfCnpj.clear();
        txtInscricaoEstadual.clear();
        txtTelefone.clear();
        txtEmail.clear();
        txtLogradouro.clear();
        txtNumero.clear();
        txtComplemento.clear();
        txtBairro.clear();
        txtCidade.clear();
        txtEstado.clear();
        txtCep.clear();
        cbStatus.setValue(null);
    }

    private void preencherFormulario(Cliente cliente) {
        txtNome.setText(cliente.getRazaoSocial());
        txtCpfCnpj.setText(cliente.getCnpj());
        txtInscricaoEstadual.setText(cliente.getInscricaoEstadual());
        txtTelefone.setText(cliente.getTelefoneCliente());
        txtEmail.setText(cliente.getEmailCliente());
        txtLogradouro.setText(cliente.getLogradouro());
        txtNumero.setText(cliente.getNumero());
        txtComplemento.setText(cliente.getComplemento());
        txtBairro.setText(cliente.getBairro());
        txtCidade.setText(cliente.getCidade());
        txtEstado.setText(cliente.getEstado());
        txtCep.setText(cliente.getCep());
        cbStatus.setValue(cliente.getStatusCliente());
    }

    private Cliente criarClienteDoFormulario() {
        Cliente cliente = modoEdicao ? clienteSelecionado : new Cliente();

        cliente.setRazaoSocial(txtNome.getText().trim());
        cliente.setCnpj(txtCpfCnpj.getText().trim());
        cliente.setInscricaoEstadual(txtInscricaoEstadual.getText().trim().isEmpty() ? null : txtInscricaoEstadual.getText().trim());
        cliente.setNomeFantasia(null); // Campo nome_fantasia não será usado para endereço
        cliente.setEmailCliente(txtEmail.getText().trim().isEmpty() ? null : txtEmail.getText().trim());
        cliente.setTelefoneCliente(txtTelefone.getText().trim().isEmpty() ? null : txtTelefone.getText().trim());

        // Campos de endereço
        cliente.setLogradouro(txtLogradouro.getText().trim().isEmpty() ? null : txtLogradouro.getText().trim());
        cliente.setNumero(txtNumero.getText().trim().isEmpty() ? null : txtNumero.getText().trim());
        cliente.setComplemento(txtComplemento.getText().trim().isEmpty() ? null : txtComplemento.getText().trim());
        cliente.setBairro(txtBairro.getText().trim().isEmpty() ? null : txtBairro.getText().trim());
        cliente.setCidade(txtCidade.getText().trim().isEmpty() ? null : txtCidade.getText().trim());
        cliente.setEstado(txtEstado.getText().trim().isEmpty() ? null : txtEstado.getText().trim());
        cliente.setCep(txtCep.getText().trim().isEmpty() ? null : txtCep.getText().trim());

        cliente.setStatusCliente(cbStatus.getValue() != null ? cbStatus.getValue().toString() : "PAGO");

        return cliente;
    }

    private boolean validarFormulario() {
        StringBuilder erros = new StringBuilder();

        if (txtNome.getText().trim().isEmpty()) {
            erros.append("- Nome/Razão Social é obrigatório\n");
        }

        if (txtCpfCnpj.getText().trim().isEmpty()) {
            erros.append("- CNPJ é obrigatório\n");
        } else {
            // Validar formato básico do CNPJ (apenas números)
            String cnpj = txtCpfCnpj.getText().trim().replaceAll("[^0-9]", "");
            if (cnpj.length() != 14) {
                erros.append("- CNPJ deve ter 14 dígitos\n");
            } else {
                try {
                    String cnpjExcluir = modoEdicao ? clienteSelecionado.getCnpj() : null;
                    if (clienteDAO.cnpjExiste(cnpj, cnpjExcluir)) {
                        erros.append("- CNPJ já cadastrado\n");
                    }
                } catch (SQLException e) {
                    erros.append("- Erro ao verificar CNPJ\n");
                }
            }
        }

        if (!txtEmail.getText().trim().isEmpty()) {
            String email = txtEmail.getText().trim();
            if (!email.contains("@") || !email.contains(".")) {
                erros.append("- E-mail deve ter formato válido\n");
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
