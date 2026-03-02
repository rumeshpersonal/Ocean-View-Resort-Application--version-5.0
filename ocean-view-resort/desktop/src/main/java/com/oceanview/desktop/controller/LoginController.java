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

  errorLabel.setText("");

  try {
    ApiClient.login(username, password);
  } catch (Exception e) {
    e.printStackTrace();
    errorLabel.setText("Login API failed: " + e.getMessage());
    return;
  }

  try {
    loadMainMenu();
  } catch (Exception e) {
    e.printStackTrace();
    errorLabel.setText("UI load failed: " + e.toString());
  }
}

  @FXML
  private void handleExit() {
    System.exit(0);
  }

 private void loadMainMenu() throws Exception {
  try {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainmenu.fxml"));
    Parent root = loader.load();

    MainMenuController controller = loader.getController();

    Stage stage = (Stage) usernameField.getScene().getWindow();

    // IMPORTANT: give the stage to MainMenuController so Logout can work
    controller.setStage(stage);

    Scene scene = new Scene(root, 900, 700);
    
    // Load and apply stylesheet
    String stylesheet = getClass().getResource("/styles.css").toExternalForm();
    scene.getStylesheets().add(stylesheet);
    
    stage.setScene(scene);
    stage.setTitle("Ocean View Resort - Main Menu");
  } catch (Exception e) {
    e.printStackTrace();
    throw e;
  }
}
}
