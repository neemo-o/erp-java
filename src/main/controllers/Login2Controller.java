package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class Login2Controller {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Text statusMessage;

    @FXML
    private void handleLoginButton() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Validações básicas
        if (username.isEmpty()) {
            statusMessage.setText("Nome de usuário não pode estar vazio.");
            statusMessage.setVisible(true);
            return;
        }

        if (password.isEmpty()) {
            statusMessage.setText("Senha não pode estar vazia.");
            statusMessage.setVisible(true);
            return;
        }

        // Lógica de login aqui (exemplo simples)
        if ("admin".equals(username) && "password".equals(password)) {
            statusMessage.setText("Login realizado com sucesso!");
            statusMessage.setVisible(true);
        } else {
            statusMessage.setText("Credenciais inválidas.");
            statusMessage.setVisible(true);
        }
    }
}
