package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class ConfiguracoesController {

    @FXML
    private TextField txtNomeEmpresa;

    @FXML
    private TextField txtCnpj;

    @FXML
    private TextField txtEndereco;

    @FXML
    private TextField txtHorarioAbertura;

    @FXML
    private TextField txtHorarioFechamento;

    @FXML
    private CheckBox chkFuncionaFds;

    @FXML
    private CheckBox chkPermitirVendaSemEstoque;

    @FXML
    private CheckBox chkRequerAutorizacaoDesconto;

    @FXML
    private CheckBox chkBloquearSistemaForaHorario;

    @FXML
    private ComboBox<?> cbNivelPermissao;

    @FXML
    private CheckBox chkBackupAutomatico;

    @FXML
    private Button btnFazerBackup;

    @FXML
    private Button btnRestaurarBackup;

    @FXML
    private Button btnSalvarConfiguracoes;

    @FXML
    private Button btnCancelar;

    @FXML
    void initialize() {
        // Código de inicialização aqui
        // Carregar configurações atuais do banco
        // Configurar níveis de permissão (Admin, Gerente, Operador, Vendedor)
    }

    @FXML
    void handleFazerBackup() {
        // Implementar lógica de fazer backup do banco de dados
    }

    @FXML
    void handleRestaurarBackup() {
        // Implementar lógica de restaurar backup
    }

    @FXML
    void handleSalvarConfiguracoes() {
        // Implementar lógica de salvar todas as configurações
    }

    @FXML
    void handleCancelar() {
        // Implementar lógica de cancelar alterações
    }

}
