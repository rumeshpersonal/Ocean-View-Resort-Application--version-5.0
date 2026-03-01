package com.oceanview.desktop.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.oceanview.desktop.api.ApiClient;

public class MainMenuController {
  private Stage stage;

  @FXML
  public void initialize() {
    // Initialization if needed
  }

  @FXML
  private void handleCreateReservation() throws Exception {
    loadScene("/fxml/createreservation.fxml", "Create Reservation");
  }

  @FXML
  private void handleViewReservation() throws Exception {
    loadScene("/fxml/viewreservation.fxml", "View Reservation");
  }

  @FXML
  private void handleGenerateBill() throws Exception {
    loadScene("/fxml/generatebill.fxml", "Generate Bill");
  }

  @FXML
  private void handleHelp() throws Exception {
    loadScene("/fxml/help.fxml", "Help & Usage Guide");
  }

  @FXML
  private void handleLogout() throws Exception {
    ApiClient.logout();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
    Parent root = loader.load();
    Scene scene = new Scene(root, 500, 400);
    Stage stage = (Stage) getStage().getScene().getWindow();
    stage.setScene(scene);
  }

  @FXML
  private void handleExit() {
    System.exit(0);
  }

  private void loadScene(String fxmlPath, String title) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
    Parent root = loader.load();
    Scene scene = new Scene(root);
    Stage newStage = new Stage();
    newStage.setTitle(title);
    newStage.setScene(scene);
    newStage.show();
  }

  private Stage getStage() {
    return (Stage) getClass().getClassLoader().toString().length() > 0 ? null : null;
  }
}
