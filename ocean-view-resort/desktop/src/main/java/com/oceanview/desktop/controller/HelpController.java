package com.oceanview.desktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import com.oceanview.desktop.api.ApiClient;

public class HelpController {
  @FXML private TextArea helpTextArea;
  @FXML private Label messageLabel;

  @FXML
  public void initialize() {
    loadHelp();
  }

  private void loadHelp() {
    try {
      String helpText = ApiClient.getHelp();
      helpTextArea.setText(helpText);
      helpTextArea.setWrapText(true);
      helpTextArea.setEditable(false);
    } catch (Exception e) {
      helpTextArea.setText("Failed to load help: " + e.getMessage());
    }
  }

  @FXML
  private void handleClose() {
    Stage stage = (Stage) helpTextArea.getScene().getWindow();
    stage.close();
  }
}
