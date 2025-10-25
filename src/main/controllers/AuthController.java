package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Text;
import main.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.UnaryOperator;

public class AuthController {

    @FXML
    private TextField documentField;

    @FXML
    private Button accessButton;

    @FXML
    private Text statusMessage;

    @FXML
    public void initialize() {
        // Limitar o campo a 18 caracteres
        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (change.getControlNewText().length() > 18) {
                return null; // Rejeitar mudança se exceder 18 caracteres
            }
            return change;
        };
        documentField.setTextFormatter(new TextFormatter<>(filter));
    }

    @FXML
    private void handleAccessButton() {
        String doc = documentField.getText().trim();

        // Validar se o CNPJ tem exatamente 18 caracteres
        if (doc.length() != 18) {
            statusMessage.setText("CNPJ deve ter exatamente 18 caracteres.");
            statusMessage.setVisible(true);
            return;
        }

        // Validar formato básico do CNPJ (exemplo: XX.XXX.XXX/XXXX-XX)
        if (!doc.matches("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}")) {
            statusMessage.setText("Formato de CNPJ inválido. Use: XX.XXX.XXX/XXXX-XX");
            statusMessage.setVisible(true);
            return;
        }

        // Só conectar ao banco se os dados forem válidos
        try {
            Connection db = DatabaseConnection.getConnectionLicenses();

            if (db != null) {
                // Verificar se o CNPJ existe e está pago
                String query = "SELECT status FROM licencas WHERE cnpj = ?";
                PreparedStatement stmt = db.prepareStatement(query);
                stmt.setString(1, doc);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String status = rs.getString("status");
                    if ("PAGO".equals(status)) {
                        statusMessage.setText("CNPJ conectado com sucesso.");
                        statusMessage.setVisible(true);
                    } else {
                        statusMessage.setText("CNPJ não pago. Acesso negado.");
                        statusMessage.setVisible(true);
                    }
                } else {
                    statusMessage.setText("CNPJ não encontrado no banco de dados.");
                    statusMessage.setVisible(true);
                }

                rs.close();
                stmt.close();
            } else {
                statusMessage.setText("Falha na conexão com o banco de dados.");
                statusMessage.setVisible(true);
            }

        } catch (SQLException e) {
            statusMessage.setText("Erro ao conectar ao banco de dados: " + e.getMessage());
            statusMessage.setVisible(true);
        }
    }
}
