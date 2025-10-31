package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DashboardController {

    @FXML
    private Label lblVendasHoje;

    @FXML
    private Label lblTotalProdutos;

    @FXML
    private Label lblTotalClientes;

    @FXML
    private Label lblVendasMes;

    @FXML
    private PieChart chartPagamentos;

    @FXML
    private VBox vboxBaixoEstoque;

    @FXML
    private Label lblUltimaAtualizacao;

    @FXML
    void initialize() {
        // Código de inicialização aqui
        // Carregar dados do dashboard
        // Configurar gráfico de pizza
        // Buscar produtos com baixo estoque
    }

}