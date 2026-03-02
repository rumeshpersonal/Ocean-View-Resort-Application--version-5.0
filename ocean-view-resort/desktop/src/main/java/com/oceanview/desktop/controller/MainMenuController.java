package com.oceanview.desktop.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.oceanview.desktop.api.ApiClient;

public class MainMenuController {
  private Stage stage;
  private static final String STYLESHEET = "/styles.css";

  @FXML
  public void initialize() {
    // Initialization if needed
  }

  @FXML
  private void handleCreateReservation() throws Exception {
    loadScene("/fxml/createreservation.fxml", "Create Reservation", 900, 700);
  }

  @FXML
  private void handleViewReservation() throws Exception {
    loadScene("/fxml/viewreservation.fxml", "View Reservation", 900, 700);
  }

  @FXML
  private void handleGenerateBill() throws Exception {
    loadScene("/fxml/generatebill.fxml", "Generate Bill", 900, 700);
  }

  @FXML
  private void handleHelp() throws Exception {
    loadScene("/fxml/help.fxml", "Help & Usage Guide", 950, 800);
  }

  @FXML
  private void handleLogout() throws Exception {
    ApiClient.logout();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
    Parent root = loader.load();
    Scene scene = new Scene(root, 900, 700);
    
    // Apply stylesheet
    String stylesheet = getClass().getResource(STYLESHEET).toExternalForm();
    scene.getStylesheets().add(stylesheet);
    
    if (stage != null) {
      stage.setScene(scene);
      stage.setTitle("Ocean View Resort - Login");
    }
  }

  @FXML
  private void handleExit() {
    System.exit(0);
  }

  private void loadScene(String fxmlPath, String title, int width, int height) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
    Parent root = loader.load();
    Scene scene = new Scene(root, width, height);
    
    // Apply stylesheet
    String stylesheet = getClass().getResource(STYLESHEET).toExternalForm();
    scene.getStylesheets().add(stylesheet);
    
    Stage newStage = new Stage();
    newStage.setTitle("Ocean View Resort - " + title);
    newStage.setScene(scene);
    newStage.setMinWidth(800);
    newStage.setMinHeight(600);
    newStage.show();
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }
}
