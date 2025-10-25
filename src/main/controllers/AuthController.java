package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class AuthController {

    @FXML
    private TextField documentField;

    @FXML
    private Button accessButton;

    @FXML
    private Text statusMessage;

    @FXML
    private void handleAccessButton() {
        String doc = documentField.getText();

        if (doc == null || doc.isBlank()) {
            statusMessage.setText("Por favor, digite seu documento.");
            statusMessage.setVisible(true);
        } else {
            statusMessage.setText("Acessando...");
            statusMessage.setVisible(true);
        }
    }
}
