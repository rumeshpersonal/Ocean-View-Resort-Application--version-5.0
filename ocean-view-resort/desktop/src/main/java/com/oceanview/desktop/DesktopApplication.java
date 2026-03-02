package com.oceanview.desktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DesktopApplication extends Application {
  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
    Parent root = loader.load();
    
    // Create scene with optimal dimensions for hotel UI
    Scene scene = new Scene(root, 900, 700);
    
    // Load global stylesheet
    String stylesheet = getClass().getResource("/styles.css").toExternalForm();
    scene.getStylesheets().add(stylesheet);
    
    stage.setTitle("Ocean View Resort - Room Reservation System");
    stage.setScene(scene);
    
    // Set minimum window size for responsive design
    stage.setMinWidth(800);
    stage.setMinHeight(600);
    
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
