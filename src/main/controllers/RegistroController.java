package main.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
    private Button registroButton;

    @FXML
    private Text statusMessage;

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

        // Inserir no banco
        try {
            Connection db = DatabaseConnection.getConnectionLicenses();
            if (db != null) {
                String query = "INSERT INTO licencas (cnpj, razao_social, nome_fantasia, inscricao_estadual, telefone, e_mail, rua, numero, bairro, cidade, estado, cep, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'PAGO')";
                PreparedStatement stmt = db.prepareStatement(query);
                stmt.setString(1, cnpj);
                stmt.setString(2, razaoSocial);
                stmt.setString(3, nomeFantasia);
                stmt.setString(4, inscricaoEstadual);
                stmt.setString(5, telefone);
                stmt.setString(6, email);
                stmt.setString(7, rua);
                stmt.setString(8, numero);
                stmt.setString(9, bairro);
                stmt.setString(10, ""); // cidade
                stmt.setString(11, ""); // estado
                stmt.setString(12, cep);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    statusMessage.setText("Cadastro realizado com sucesso! Aguarde aprovação.");
                    statusMessage.setVisible(true);
                } else {
                    statusMessage.setText("Erro ao cadastrar.");
                    statusMessage.setVisible(true);
                }
                stmt.close();
            } else {
                statusMessage.setText("Falha na conexão com o banco.");
                statusMessage.setVisible(true);
            }
        } catch (SQLException e) {
            statusMessage.setText("Erro: " + e.getMessage());
            statusMessage.setVisible(true);
        }
    }

    @FXML
    private void handleLoginLink() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/view/Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) registroButton.getScene().getWindow();
            stage.centerOnScreen();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            statusMessage.setText("Erro ao voltar para login: " + e.getMessage());
            statusMessage.setVisible(true);
        }
    }
}
