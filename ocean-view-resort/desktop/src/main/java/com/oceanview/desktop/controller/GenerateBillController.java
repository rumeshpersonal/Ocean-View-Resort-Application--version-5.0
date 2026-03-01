package com.oceanview.desktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import com.oceanview.desktop.api.ApiClient;

public class GenerateBillController {
  @FXML private TextField reservationNoField;
  @FXML private Label nightsLabel;
  @FXML private Label rateLabel;
  @FXML private Label totalLabel;
  @FXML private Label messageLabel;

  @FXML
  private void handleGenerateBill() {
    String reservationNo = reservationNoField.getText().trim();

    if (reservationNo.isEmpty()) {
      messageLabel.setText("Error: Reservation number is required");
      return;
    }

    try {
      ApiClient.BillResponse bill = ApiClient.generateBill(reservationNo);
      nightsLabel.setText("Nights: " + bill.nights);
      rateLabel.setText("Rate/Night: Rs. " + String.format("%.2f", bill.ratePerNight));
      totalLabel.setText("Total Amount: Rs. " + String.format("%.2f", bill.totalAmount));
      messageLabel.setText("Bill generated successfully");
    } catch (Exception e) {
      messageLabel.setText("Error: " + e.getMessage());
      clearLabels();
    }
  }

  @FXML
  private void handleCancel() {
    Stage stage = (Stage) reservationNoField.getScene().getWindow();
    stage.close();
  }

  private void clearLabels() {
    nightsLabel.setText("Nights: -");
    rateLabel.setText("Rate/Night: -");
    totalLabel.setText("Total Amount: -");
  }
}
