package main.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.database.DatabaseConnection;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RegistroController {

    @FXML
    private TextField cnpjField;

    @FXML
    private TextField razaoSocialField;

    @FXML
    private TextField nomeFantasiaField;

    @FXML
    private TextField inscricaoEstadualField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField telefoneField;

    @FXML
    private TextField cepField;

    @FXML
    private TextField ruaField;

    @FXML
    private TextField numeroField;

    @FXML
    private TextField bairroField;

    @FXML
    private TextField cidadeField;

    @FXML
    private TextField estadoField;

    @FXML
    private Button registroButton;

    @FXML
    private Text statusMessage;

    @FXML
    public void initialize() {
        // Desabilitar resize da janela
        cnpjField.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) newScene.getWindow();
                if (stage != null) {
                    stage.setResizable(false);
                }
            }
        });
    }

    // GERAR ID CREDENCIAL
    private Integer gerarIDCredencial(String cnpj) {
        Integer id = null;
        Connection db = null;
        try {
            db = DatabaseConnection.getConnectionLicenses();
            if (db != null) {
                String query = "SELECT COALESCE(MAX(ID_Credenciais), 0) + 1 AS next_id FROM licenca_credenciais WHERE cnpj = ?";
                PreparedStatement stmt = db.prepareStatement(query);
                stmt.setString(1, cnpj);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    id = rs.getInt("next_id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao gerar ID de credencial: " + e.getMessage());
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }
        return id;
    }

    /**
     * Gera uma senha aleatória de 10 caracteres alfanuméricos
     */
    private String gerarSenhaAleatoria() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder senha = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {
            senha.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }

        return senha.toString();
    }

    /**
     * Gera um nome de usuário baseado no CNPJ
     */
    private String gerarNomeUsuario(String cnpj) {
        // Remove pontos, barras e traços do CNPJ e pega os primeiros 8 dígitos
        String cnpjLimpo = cnpj.replaceAll("[./-]", "");
        return "user" + cnpjLimpo.substring(0, Math.min(8, cnpjLimpo.length()));
    }

    @FXML
    private void handleRegistro() {
        String cnpj = cnpjField.getText().trim();
        String razaoSocial = razaoSocialField.getText().trim();
        String nomeFantasia = nomeFantasiaField.getText().trim();
        String inscricaoEstadual = inscricaoEstadualField.getText().trim();
        String email = emailField.getText().trim();
        String telefone = telefoneField.getText().trim();
        String cep = cepField.getText().trim();
        String rua = ruaField.getText().trim();
        String numero = numeroField.getText().trim();
        String bairro = bairroField.getText().trim();
        String cidade = cidadeField.getText().trim(); // Campo não utilizado
        String estado = estadoField.getText().trim();

        // Validações básicas
        if (cnpj.isEmpty() || razaoSocial.isEmpty() || email.isEmpty()) {
            statusMessage.setText("CNPJ, Razão Social e E-mail são obrigatórios.");
            statusMessage.setVisible(true);
            return;
        }

        if (cnpj.length() != 18 || !cnpj.matches("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}")) {
            statusMessage.setText("CNPJ deve ter formato XX.XXX.XXX/XXXX-XX e 18 caracteres.");
            statusMessage.setVisible(true);
            return;
        }

        // Inserir no banco - primeiro a empresa, depois as credenciais
        Connection db = null;
        try {
            db = DatabaseConnection.getConnectionLicenses();
            if (db != null) {
                // Desabilitar auto-commit para usar transação
                db.setAutoCommit(false);

                // PASSO 1: Inserir a empresa na tabela licencas (sem credencial ainda)
                String queryLicenca = "INSERT INTO licencas (cnpj, razao_social, nome_fantasia, inscricao_estadual, telefone, e_mail, rua, numero, bairro, cidade, estado, cep, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'PAGO')";
                PreparedStatement stmtLicenca = db.prepareStatement(queryLicenca);
                stmtLicenca.setString(1, cnpj);
                stmtLicenca.setString(2, razaoSocial);
                stmtLicenca.setString(3, nomeFantasia);
                stmtLicenca.setString(4, inscricaoEstadual);
                stmtLicenca.setString(5, telefone);
                stmtLicenca.setString(6, email);
                stmtLicenca.setString(7, rua);
                stmtLicenca.setString(8, numero);
                stmtLicenca.setString(9, bairro);
                stmtLicenca.setString(10, cidade);
                stmtLicenca.setString(11, estado);
                stmtLicenca.setString(12, cep);

                int licencaRows = stmtLicenca.executeUpdate();
                stmtLicenca.close();

                if (licencaRows > 0) {
                    // PASSO 2: Empresa cadastrada com sucesso, agora criar as credenciais

                    // Gerar credenciais para o usuário administrador
                    Integer idCredencial = gerarIDCredencial(cnpj);
                    String nomeUsuario = gerarNomeUsuario(cnpj);
                    String senha = gerarSenhaAleatoria();

                    // Inserir as credenciais na tabela licenca_credenciais
                    String queryCredenciais = "INSERT INTO licenca_credenciais (ID_Credenciais, cnpj, credenciais, senha, tipo_usuario) VALUES (?, ?, ?, ?, 'ADM')";
                    PreparedStatement stmtCredenciais = db.prepareStatement(queryCredenciais, Statement.RETURN_GENERATED_KEYS);
                    stmtCredenciais.setString(1, cnpj);
                    stmtCredenciais.setString(2, nomeUsuario);
                    stmtCredenciais.setString(3, senha);

                    int credenciaisRows = stmtCredenciais.executeUpdate();
                    int idCredencial = -1;

                    if (credenciaisRows > 0) {
                        // Obter o ID gerado da credencial
                        ResultSet generatedKeys = stmtCredenciais.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            idCredencial = generatedKeys.getInt(1);
                        }
                        generatedKeys.close();
                    }
                    stmtCredenciais.close();

                    if (idCredencial > 0) {
                        // PASSO 3: Atualizar a empresa com o ID da credencial principal
                        String queryUpdate = "UPDATE licencas SET id_credencial_principal = ? WHERE cnpj = ?";
                        PreparedStatement stmtUpdate = db.prepareStatement(queryUpdate);
                        stmtUpdate.setInt(1, idCredencial);
                        stmtUpdate.setString(2, cnpj);
                        stmtUpdate.executeUpdate();
                        stmtUpdate.close();

                        // Confirmar a transação
                        db.commit();

                        statusMessage.setText("Cadastro realizado com sucesso!");
                        statusMessage.setVisible(true);

                        // Exibir as credenciais geradas em um Alert
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Credenciais Criadas");
                        alert.setHeaderText("Seu cadastro foi realizado com sucesso!");
                        alert.setContentText(String.format(
                            "Credenciais de acesso:\n\n" +
                            "Usuário: %s\n" +
                            "Senha: %s\n\n" +
                            "Tipo: Administrador\n\n" +
                            "Guarde essas informações em local seguro.\n" +
                            "Você poderá alterar a senha após o primeiro login.",
                            nomeUsuario, senha
                        ));
                        alert.showAndWait();

                    } else {
                        // Falhou ao criar credenciais, fazer rollback
                        db.rollback();
                        statusMessage.setText("Erro ao criar credenciais para a empresa.");
                        statusMessage.setVisible(true);
                    }

                } else {
                    statusMessage.setText("Erro ao cadastrar empresa.");
                    statusMessage.setVisible(true);
                }

            } else {
                statusMessage.setText("Falha na conexão com o banco.");
                statusMessage.setVisible(true);
            }
        } catch (SQLException e) {
            // Em caso de erro, fazer rollback
            if (db != null) {
                try {
                    db.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Erro no rollback: " + rollbackEx.getMessage());
                }
            }
            statusMessage.setText("Erro: " + e.getMessage());
            statusMessage.setVisible(true);
        } finally {
            // Restaurar auto-commit e fechar conexão
            if (db != null) {
                try {
                    db.setAutoCommit(true);
                    db.close();
                } catch (SQLException closeEx) {
                    System.err.println("Erro ao fechar conexão: " + closeEx.getMessage());
                }
            }
        }
    }

    @FXML
    private void handleLoginLink() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/view/Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) registroButton.getScene().getWindow();
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            statusMessage.setText("Erro ao voltar para login: " + e.getMessage());
            statusMessage.setVisible(true);
        }
    }
}
