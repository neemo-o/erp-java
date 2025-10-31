package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class VendasController {

    @FXML
    private TextField txtCodigoBarras;

    @FXML
    private TextField txtQuantidade;

    @FXML
    private Button btnAdicionar;

    @FXML
    private TableView<?> tableItens;

    @FXML
    private TableColumn<?, ?> colCodigo;

    @FXML
    private TableColumn<?, ?> colDescricao;

    @FXML
    private TableColumn<?, ?> colQuantidade;

    @FXML
    private TableColumn<?, ?> colPrecoUnit;

    @FXML
    private TableColumn<?, ?> colTotal;

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
    private ComboBox<?> cbFormaPagamento;

    @FXML
    private ComboBox<?> cbCliente;

    @FXML
    private Button btnFinalizarVenda;

    @FXML
    private Button btnCancelarVenda;

    @FXML
    private Label lblVendaAtual;

    @FXML
    private Label lblOperador;

    @FXML
    void initialize() {
        // Código de inicialização aqui
        // Configurar formas de pagamento
        // Carregar clientes
        // Configurar tabela de itens
    }

    @FXML
    void handleAdicionar() {
        // Implementar lógica de adicionar produto à venda
    }

    @FXML
    void handleRemoverItem() {
        // Implementar lógica de remover item selecionado
    }

    @FXML
    void handleLimparVenda() {
        // Implementar lógica de limpar toda a venda
    }

    @FXML
    void handleFinalizarVenda() {
        // Implementar lógica de finalizar e salvar venda
    }

    @FXML
    void handleCancelarVenda() {
        // Implementar lógica de cancelar venda
    }

}
