package com.example.controllers;

import com.example.models.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.*;

public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMessage;

    @FXML
    public void initialize() {
        // Initialize DB tables automatically
        Database.initialize();
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Please enter username and password!");
            return;
        }

        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // ✅ Successful login
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/billing/lec_billing.fxml"));
                Stage stage = (Stage) txtUsername.getScene().getWindow();
                Scene scene = new Scene(loader.load());
                scene.getStylesheets().add(getClass().getResource("/com/example/billing/style.css").toExternalForm());
                stage.setScene(scene);
                stage.setTitle("LEC Billing Dashboard");
            } else {
                lblMessage.setText("Invalid username or password!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Login failed. Try again.");
        }
    }

    @FXML
    private void handleGoToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/billing/register.fxml"));
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/com/example/billing/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Register - LEC Billing System");
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Cannot open register screen.");
        }
    }
}
