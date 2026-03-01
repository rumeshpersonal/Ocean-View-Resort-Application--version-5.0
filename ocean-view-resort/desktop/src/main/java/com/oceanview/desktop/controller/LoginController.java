package com.oceanview.desktop.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import com.oceanview.desktop.api.ApiClient;

public class LoginController {
  @FXML private TextField usernameField;
  @FXML private PasswordField passwordField;
  @FXML private Label errorLabel;

  @FXML
  private void handleLogin() {
    String username = usernameField.getText().trim();
    String password = passwordField.getText();

    if (username.isEmpty() || password.isEmpty()) {
      errorLabel.setText("Username and password are required");
      return;
    }

    try {
      ApiClient.login(username, password);
      loadMainMenu();
    } catch (Exception e) {
      errorLabel.setText("Login failed: " + e.getMessage());
    }
  }

  @FXML
  private void handleExit() {
    System.exit(0);
  }

  private void loadMainMenu() throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainmenu.fxml"));
    Parent root = loader.load();
    MainMenuController controller = loader.getController();
    Stage stage = (Stage) usernameField.getScene().getWindow();
    controller.setStage(stage);
    Scene scene = new Scene(root, 600, 500);
    stage.setScene(scene);
  }
}
