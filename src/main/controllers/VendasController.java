package main.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.util.Duration;
import main.database.ClienteDAO;
import main.database.ItemVendaDAO;
import main.database.ProdutoDAO;
import main.database.VendasDAO;
import main.models.Cliente;
import main.models.ItemVenda;
import main.models.Produto;
import main.models.Vendas;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class VendasController {

    @FXML
    private TextField txtCodigoBarras;

    @FXML
    private TextField txtQuantidade;

    @FXML
    private Button btnAdicionar;

    @FXML
    private TableView<ItemVenda> tableItens;

    @FXML
    private TableColumn<ItemVenda, String> colCodigo;

    @FXML
    private TableColumn<ItemVenda, String> colDescricao;

    @FXML
    private TableColumn<ItemVenda, Integer> colQuantidade;

    @FXML
    private TableColumn<ItemVenda, BigDecimal> colPrecoUnit;

    @FXML
    private TableColumn<ItemVenda, BigDecimal> colTotal;

    @FXML
    private Button btnRemoverItem;

    @FXML
    private Button btnLimparVenda;

    @FXML
    private Label lblSubtotal;

    @FXML
    private Label lblDesconto;

    @FXML
    private Label lblTotal;

    @FXML
    private ComboBox<String> cbFormaPagamento;

    @FXML
    private ComboBox<Cliente> cbCliente;

    @FXML
    private Button btnFinalizarVenda;

    @FXML
    private Button btnCancelarVenda;

    @FXML
    private Label lblVendaAtual;

    @FXML
    private Label lblOperador;

    private VendasDAO vendasDAO;
    private ItemVendaDAO itemVendaDAO;
    private ProdutoDAO produtoDAO;
    private ClienteDAO clienteDAO;
    private ObservableList<ItemVenda> itensVenda;
    private ObservableList<Cliente> clientes;
    private ObservableList<String> formasPagamento;
    private Vendas vendaAtual;
    private BigDecimal subtotal = BigDecimal.ZERO;
    private BigDecimal desconto = BigDecimal.ZERO;

    @FXML
    void initialize() {
        vendasDAO = new VendasDAO();
        itemVendaDAO = new ItemVendaDAO();
        produtoDAO = new ProdutoDAO();
        clienteDAO = new ClienteDAO();

        itensVenda = FXCollections.observableArrayList();
        clientes = FXCollections.observableArrayList();
        formasPagamento = FXCollections.observableArrayList();

        configurarTableView();
        configurarComboBoxes();
        aplicarMascaras();
        aplicarValidacoes();

        carregarClientes();
        configurarFormasPagamento();
        iniciarNovaVenda();

        configurarFocoCampo();
        atualizarTotais();

        lblOperador.setText("Operador: Sistema");
    }

    private void configurarTableView() {
        colCodigo.setCellValueFactory(cellData -> {
            ItemVenda item = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(
                item.getProduto() != null ? item.getProduto().getCodigoBarras() : "");
        });

        colDescricao.setCellValueFactory(cellData -> {
            ItemVenda item = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(
                item.getProduto() != null ? item.getProduto().getDescricao() : "");
        });

        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colPrecoUnit.setCellValueFactory(new PropertyValueFactory<>("precoUnitario"));

        colTotal.setCellValueFactory(cellData -> {
            ItemVenda item = cellData.getValue();
            BigDecimal subtotal = item.getSubtotal();
            return new javafx.beans.property.SimpleObjectProperty<>(subtotal);
        });

        colPrecoUnit.setCellFactory(column -> new TableCell<ItemVenda, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("R$ %.2f", item));
            }
        });

        colTotal.setCellFactory(column -> new TableCell<ItemVenda, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("R$ %.2f", item));
            }
        });

        tableItens.setItems(itensVenda);
    }

    private void configurarComboBoxes() {
        formasPagamento.addAll("PIX", "CARTAO", "DINHEIRO");
        cbFormaPagamento.setItems(formasPagamento);
        cbFormaPagamento.setValue("DINHEIRO");

        cbCliente.setItems(clientes);

        cbCliente.setCellFactory(param -> new ListCell<Cliente>() {
            @Override
            protected void updateItem(Cliente item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Sem cliente" : item.getRazaoSocial());
            }
        });

        cbCliente.setButtonCell(new ListCell<Cliente>() {
            @Override
            protected void updateItem(Cliente item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Sem cliente" : item.getRazaoSocial());
            }
        });
    }

    private void aplicarMascaras() {
        // Apenas números para quantidade
        txtQuantidade.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.matches("\\d*")) {
                txtQuantidade.setText(newVal.replaceAll("[^\\d]", ""));
            }
            if (!newVal.isEmpty()) {
                try {
                    int quantidade = Integer.parseInt(newVal);
                    if (quantidade <= 0) {
                        txtQuantidade.setText(oldVal);
                    }
                } catch (NumberFormatException e) {
                    txtQuantidade.setText(oldVal);
                }
            }
        });

        // Apenas números para código de barras
        txtCodigoBarras.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.matches("\\d*")) {
                txtCodigoBarras.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void aplicarValidacoes() {
        validarCampoObrigatorio(txtQuantidade);
        validarCampoObrigatorio(txtCodigoBarras);
    }

    private void validarCampoObrigatorio(TextField field) {
        field.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                if (field.getText().trim().isEmpty()) {
                    field.setStyle("-fx-border-color: #e57373; -fx-border-width: 2; -fx-background-color: white; -fx-padding: 6;");
                } else {
                    field.setStyle("-fx-border-color: #7cb342; -fx-border-width: 2; -fx-background-color: white; -fx-padding: 6;");
                }
            }
        });

        field.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.trim().isEmpty() && field.getStyle().contains("#e57373")) {
                field.setStyle("-fx-border-color: #7cb342; -fx-border-width: 2; -fx-background-color: white; -fx-padding: 6;");
            }
        });
    }

    private void mostrarNotificacao(String titulo, String mensagem, String tipo) {
        if (txtCodigoBarras.getScene() == null || txtCodigoBarras.getScene().getWindow() == null) return;

        Popup popup = new Popup();
        String cor = tipo.equals("sucesso") ? "#7cb342" : tipo.equals("erro") ? "#e57373" : "#ffb74d";
        String icone = tipo.equals("sucesso") ? "✓" : tipo.equals("erro") ? "✗" : "⚠";

        VBox container = new VBox(5);
        container.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 5;" +
            "-fx-border-color: " + cor + ";" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 5;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);"
        );
        container.setPadding(new Insets(15));
        container.setMaxWidth(350);

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(icone);
        iconLabel.setStyle("-fx-text-fill: " + cor + "; -fx-font-size: 20px; -fx-font-weight: bold;");

        Label titleLabel = new Label(titulo);
        titleLabel.setStyle("-fx-text-fill: " + cor + "; -fx-font-size: 14px; -fx-font-weight: bold;");
        titleLabel.setFont(Font.font("Segoe UI", 14));

        header.getChildren().addAll(iconLabel, titleLabel);

        Label messageLabel = new Label(mensagem);
        messageLabel.setWrapText(true);
        messageLabel.setStyle("-fx-text-fill: #333; -fx-font-size: 12px;");
        messageLabel.setFont(Font.font("Segoe UI", 12));
        messageLabel.setMaxWidth(320);

        container.getChildren().addAll(header, messageLabel);
        popup.getContent().add(container);

        popup.setAutoHide(true);
        popup.show(txtCodigoBarras.getScene().getWindow(),
            txtCodigoBarras.getScene().getWindow().getX() + txtCodigoBarras.getScene().getWindow().getWidth() - 380,
            txtCodigoBarras.getScene().getWindow().getY() + 60
        );

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), container);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        PauseTransition pause = new PauseTransition(Duration.millis(3000));

        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), container);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> popup.hide());

        SequentialTransition sequence = new SequentialTransition(fadeIn, pause, fadeOut);
        sequence.play();
    }

    private void configurarFocoCampo() {
        txtCodigoBarras.setOnAction(event -> {
            if (!txtCodigoBarras.getText().trim().isEmpty()) {
                txtQuantidade.requestFocus();
            }
        });

        txtQuantidade.setOnAction(event -> {
            btnAdicionar.fire();
        });
    }

    private void carregarClientes() {
        try {
            List<Cliente> listaClientes = clienteDAO.buscarTodos();
            clientes.clear();
            clientes.addAll(listaClientes);
        } catch (SQLException e) {
            mostrarNotificacao("Erro", "Erro ao carregar clientes", "erro");
        }
    }

    private void configurarFormasPagamento() {
        cbFormaPagamento.setItems(formasPagamento);
    }

    private void iniciarNovaVenda() {
        itensVenda.clear();
        vendaAtual = new Vendas();
        vendaAtual.setDataVenda(new Timestamp(System.currentTimeMillis()));
        subtotal = BigDecimal.ZERO;
        desconto = BigDecimal.ZERO;
        lblVendaAtual.setText("Nova Venda");
        limparCampos();
        atualizarTotais();
    }

    private void limparCampos() {
        txtCodigoBarras.clear();
        txtQuantidade.setText("1");
        cbCliente.setValue(null);
        cbFormaPagamento.setValue("DINHEIRO");

        // Resetar estilos
        txtCodigoBarras.setStyle("-fx-background-color: white; -fx-border-color: #d0d0d0; -fx-border-width: 1; -fx-padding: 6;");
        txtQuantidade.setStyle("-fx-background-color: white; -fx-border-color: #d0d0d0; -fx-border-width: 1; -fx-padding: 6;");
    }

    private void atualizarTotais() {
        BigDecimal totalItens = itensVenda.stream()
            .map(ItemVenda::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        subtotal = totalItens;
        BigDecimal totalFinal = subtotal.subtract(desconto);

        lblSubtotal.setText(String.format("R$ %.2f", subtotal));
        lblDesconto.setText(String.format("R$ %.2f", desconto));
        lblTotal.setText(String.format("R$ %.2f", totalFinal));

        vendaAtual.setValorTotal(totalFinal);
    }

    private boolean validarAdicaoProduto() {
        String codigoBarras = txtCodigoBarras.getText().trim();
        String quantidadeStr = txtQuantidade.getText().trim();
        List<String> erros = new ArrayList<>();

        if (codigoBarras.isEmpty()) {
            erros.add("Código de barras é obrigatório");
        }

        if (quantidadeStr.isEmpty()) {
            erros.add("Quantidade é obrigatória");
        } else {
            try {
                int quantidade = Integer.parseInt(quantidadeStr);
                if (quantidade <= 0) {
                    erros.add("Quantidade deve ser maior que zero");
                } else if (quantidade > 1000) {
                    erros.add("Quantidade muito alta");
                }
            } catch (NumberFormatException e) {
                erros.add("Quantidade inválida");
            }
        }

        if (codigoBarras.length() > 50) {
            erros.add("Código de barras muito longo");
        }

        if (!erros.isEmpty()) {
            mostrarNotificacao("Corrija os erros", String.join("\n", erros), "erro");
            return false;
        }

        return true;
    }

    @FXML
    void handleAdicionar() {
        if (!validarAdicaoProduto()) {
            return;
        }

        try {
            Produto produto = produtoDAO.buscarPorCodigoBarras(txtCodigoBarras.getText().trim());

            if (produto == null) {
                mostrarNotificacao("Erro", "Produto não encontrado", "erro");
                txtCodigoBarras.requestFocus();
                return;
            }

            int quantidade = Integer.parseInt(txtQuantidade.getText().trim());

            if (produto.getEstoqueAtual() < quantidade) {
                mostrarNotificacao("Erro", "Estoque insuficiente. Disponível: " + produto.getEstoqueAtual(), "erro");
                txtQuantidade.requestFocus();
                return;
            }

            // Verificar se o produto já está na lista
            ItemVenda itemExistente = itensVenda.stream()
                .filter(item -> item.getIdProduto() == produto.getIdProduto())
                .findFirst()
                .orElse(null);

            if (itemExistente != null) {
                itemExistente.setQuantidade(itemExistente.getQuantidade() + quantidade);
            } else {
                ItemVenda novoItem = new ItemVenda();
                novoItem.setIdProduto(produto.getIdProduto());
                novoItem.setProduto(produto); // Para facilitar acesso no TableView
                novoItem.setQuantidade(quantidade);
                novoItem.setPrecoUnitario(produto.getPrecoVenda());
                novoItem.setDataCadastro(new Timestamp(System.currentTimeMillis()));
                itensVenda.add(novoItem);
            }

            mostrarNotificacao("Sucesso", "Produto adicionado à venda", "sucesso");
            limparCampos();
            atualizarTotais();
            txtCodigoBarras.requestFocus();

        } catch (SQLException e) {
            mostrarNotificacao("Erro", "Erro ao buscar produto", "erro");
        }
    }

    @FXML
    void handleRemoverItem() {
        ItemVenda itemSelecionado = tableItens.getSelectionModel().getSelectedItem();
        if (itemSelecionado == null) {
            mostrarNotificacao("Atenção", "Selecione um item para remover", "aviso");
            return;
        }

        itensVenda.remove(itemSelecionado);
        atualizarTotais();
        mostrarNotificacao("Sucesso", "Item removido da venda", "sucesso");
    }

    @FXML
    void handleLimparVenda() {
        if (!itensVenda.isEmpty()) {
            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacao.setTitle("Confirmar limpeza");
            confirmacao.setHeaderText("Deseja limpar toda a venda?");
            confirmacao.setContentText("Todos os itens serão removidos.");

            if (confirmacao.showAndWait().get() == ButtonType.OK) {
                itensVenda.clear();
                atualizarTotais();
                mostrarNotificacao("Sucesso", "Venda limpa", "sucesso");
            }
        }
    }

    @FXML
    void handleFinalizarVenda() {
        if (itensVenda.isEmpty()) {
            mostrarNotificacao("Atenção", "Adicione pelo menos um produto à venda", "aviso");
            return;
        }

        if (cbFormaPagamento.getValue() == null) {
            mostrarNotificacao("Atenção", "Selecione a forma de pagamento", "aviso");
            cbFormaPagamento.requestFocus();
            return;
        }

        try {
            // Salvar venda
            vendaAtual.setFormaPagamento(cbFormaPagamento.getValue());
            int idVenda = vendasDAO.salvar(vendaAtual);

            if (idVenda <= 0) {
                mostrarNotificacao("Erro", "Erro ao salvar venda", "erro");
                return;
            }

            vendaAtual.setIdVenda(idVenda);

            // Salvar itens da venda
            for (ItemVenda item : itensVenda) {
                item.setIdVenda(idVenda);
                itemVendaDAO.salvar(item);

                // Atualizar estoque do produto
                Produto produto = item.getProduto();
                if (produto != null) {
                    produto.setEstoqueAtual(produto.getEstoqueAtual() - item.getQuantidade());
                    produtoDAO.atualizar(produto);
                }
            }

            mostrarNotificacao("Sucesso", "Venda finalizada com sucesso!", "sucesso");
            lblVendaAtual.setText("Venda #" + idVenda);
            iniciarNovaVenda(); // Preparar para nova venda

        } catch (SQLException e) {
            mostrarNotificacao("Erro", "Erro ao finalizar venda", "erro");
        }
    }

    @FXML
    void handleCancelarVenda() {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Cancelar venda");
        confirmacao.setHeaderText("Deseja cancelar a venda atual?");
        confirmacao.setContentText("Todos os itens serão perdidos.");

        if (confirmacao.showAndWait().get() == ButtonType.OK) {
            iniciarNovaVenda();
            mostrarNotificacao("Cancelado", "Venda cancelada", "aviso");
        }
    }
}
