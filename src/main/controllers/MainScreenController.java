package main.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainScreenController {

    @FXML
    private Button minimizeButton;

    @FXML
    private Button closeButton;

    @FXML
    private Button btnProdutos;

    @FXML
    private Button btnClientes;

    @FXML
    private Button btnVendas;

    @FXML
    private Button btnEstoque;

    @FXML
    private Button btnFornecedores;

    @FXML
    private Button btnRelatorios;

    @FXML
    private Button btnConfiguracoes;

    @FXML
    private StackPane contentArea;

    @FXML
    private Label usuarioLabel;

    @FXML
    private Label ipLabel;

    @FXML
    private Label versaoLabel;

    @FXML
    private Label dataHoraLabel;

    @FXML
    private Text vendasHojeText;

    @FXML
    private Text produtosText;

    @FXML
    private Text clientesText;

    private Button currentActiveButton;

   @FXML
public void initialize() {
    System.out.println("=== INITIALIZE MainScreenController ===");
    
    // IMPORTANTE: Configurar janela IMEDIATAMENTE quando a cena estiver disponível
    Platform.runLater(() -> {
        try {
            if (contentArea.getScene() != null && contentArea.getScene().getWindow() != null) {
                Stage stage = (Stage) contentArea.getScene().getWindow();
                System.out.println("Configurando stage...");
                stage.setResizable(false);
                stage.setMaximized(true);
                System.out.println("Stage maximizado: " + stage.isMaximized());
            }
        } catch (Exception e) {
            System.err.println("Erro ao maximizar: " + e.getMessage());
            e.printStackTrace();
        }
    });

    // Obter IP da máquina
    try {
        InetAddress ip = InetAddress.getLocalHost();
        ipLabel.setText(ip.getHostAddress());
    } catch (Exception e) {
        ipLabel.setText("N/A");
    }

    // Iniciar relógio em tempo real
    Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        dataHoraLabel.setText(now.format(formatter));
    }), new KeyFrame(Duration.seconds(1)));
    clock.setCycleCount(Animation.INDEFINITE);
    clock.play();

    // Carregar dados iniciais do dashboard
    carregarDashboard();

    // Adicionar efeitos hover aos botões do menu
    adicionarEfeitosMenu();
    
    System.out.println("=== INITIALIZE CONCLUÍDO ===");
}

private void adicionarEfeitosMenu() {
    System.out.println("Adicionando efeitos ao menu...");
    
    Button[] menuButtons = {btnProdutos, btnClientes, btnVendas, btnEstoque, 
                            btnFornecedores, btnRelatorios, btnConfiguracoes};

    for (int i = 0; i < menuButtons.length; i++) {
        Button btn = menuButtons[i];
        
        if (btn == null) {
            System.err.println("ERRO: Botão na posição " + i + " está NULL");
            continue;
        }
        
        btn.setOnMouseEntered(e -> {
            if (btn != currentActiveButton) {
                btn.setStyle(btn.getStyle() + "-fx-background-color: #e8f4f8;");
            }
        });

        btn.setOnMouseExited(e -> {
            if (btn != currentActiveButton) {
                btn.setStyle(btn.getStyle().replace("-fx-background-color: #e8f4f8;", "-fx-background-color: transparent;"));
            }
        });
    }
    
    System.out.println("Efeitos adicionados com sucesso");
}

    private void setActiveButton(Button button) {
        // Remover destaque do botão anterior
        if (currentActiveButton != null) {
            String style = currentActiveButton.getStyle();
            currentActiveButton.setStyle(style.replace("-fx-border-color: #4fa8d8;", "-fx-border-color: transparent;")
                                              .replace("-fx-background-color: #e8f4f8;", "-fx-background-color: transparent;"));
        }

        // Destacar novo botão ativo
        currentActiveButton = button;
        String style = button.getStyle();
        button.setStyle(style + "-fx-border-color: #4fa8d8; -fx-background-color: #e8f4f8;");
    }

    private void carregarDashboard() {
        // Aqui você pode carregar dados reais do banco de dados
        // Por enquanto, valores de exemplo
        vendasHojeText.setText("R$ 1.234,56");
        produtosText.setText("250");
        clientesText.setText("87");
    }

    @FXML
    private void handleProdutos() {
        setActiveButton(btnProdutos);
        carregarTela("/main/view/Produtos.fxml");
    }

    @FXML
    private void handleClientes() {
        setActiveButton(btnClientes);
        carregarTela("/main/view/Clientes.fxml");
    }

    @FXML
    private void handleVendas() {
        setActiveButton(btnVendas);
        carregarTela("/main/view/Vendas.fxml");
    }

    @FXML
    private void handleEstoque() {
        setActiveButton(btnEstoque);
        carregarTela("/main/view/Estoque.fxml");
    }

    @FXML
    private void handleFornecedores() {
        setActiveButton(btnFornecedores);
        carregarTela("/main/view/Fornecedores.fxml");
    }

    @FXML
    private void handleRelatorios() {
        setActiveButton(btnRelatorios);
        carregarTela("/main/view/Relatorios.fxml");
    }

    @FXML
    private void handleConfiguracoes() {
        setActiveButton(btnConfiguracoes);
        carregarTela("/main/view/Configuracoes.fxml");
    }

    private void carregarTela(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent telaCarregada = loader.load();
            
            // Limpar área de conteúdo e adicionar nova tela
            contentArea.getChildren().clear();
            contentArea.getChildren().add(telaCarregada);
        } catch (Exception e) {
            System.err.println("Erro ao carregar tela: " + fxmlPath);
            e.printStackTrace();
            // Mostrar mensagem de erro na área de conteúdo
            Text errorText = new Text("Erro ao carregar módulo: " + e.getMessage());
            errorText.setStyle("-fx-fill: #d32f2f; -fx-font-size: 14;");
            contentArea.getChildren().clear();
            contentArea.getChildren().add(errorText);
        }
    }

    @FXML
    private void handleMinimize() {
        if (minimizeButton.getScene() != null && minimizeButton.getScene().getWindow() != null) {
            Stage stage = (Stage) minimizeButton.getScene().getWindow();
            stage.setIconified(true);
        }
    }

    @FXML
    private void handleClose() {
        Platform.exit();
    }

    // Método público para atualizar o usuário logado
    public void setUsuario(String nomeUsuario) {
        usuarioLabel.setText(nomeUsuario);
    }

    // Método público para atualizar dados do dashboard
    public void atualizarDashboard(String vendas, String produtos, String clientes) {
        if (vendas != null) vendasHojeText.setText(vendas);
        if (produtos != null) produtosText.setText(produtos);
        if (clientes != null) clientesText.setText(clientes);
    }
}
