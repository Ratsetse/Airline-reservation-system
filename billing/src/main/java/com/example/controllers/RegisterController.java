package com.example.controllers;

import com.example.models.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.*;

public class RegisterController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMessage;

    @FXML
    private void handleRegister() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Please fill all fields!");
            return;
        }

        String query = "INSERT INTO users(username, password) VALUES (?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();

            lblMessage.setText("✅ Registration successful! You can now log in.");

        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint")) {
                lblMessage.setText("⚠ Username already exists!");
            } else {
                e.printStackTrace();
                lblMessage.setText("Error registering user.");
            }
        }
    }

    @FXML
    private void handleBackToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/billing/login.fxml"));
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/com/example/billing/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Login - LEC Billing System");
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Cannot open login screen.");
        }
    }
}
