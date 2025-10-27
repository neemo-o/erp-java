package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
    public void initialize() {
        
        usernameField.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) newScene.getWindow();
                if (stage != null) {
                    stage.setResizable(false);
                }
            }
        });
    }

    @FXML
    private void handleLoginButton() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        
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

        
        if ("admin".equals(username) && "password".equals(password)) {
            statusMessage.setText("Login realizado com sucesso!");
            statusMessage.setVisible(true);
        } else {
            statusMessage.setText("Credenciais inválidas.");
            statusMessage.setVisible(true);
        }
    }
}