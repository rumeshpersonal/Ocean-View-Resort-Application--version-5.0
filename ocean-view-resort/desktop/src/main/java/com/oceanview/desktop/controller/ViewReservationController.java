package com.oceanview.desktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import com.oceanview.desktop.api.ApiClient;

public class ViewReservationController {
  @FXML private TextField reservationNoField;
  @FXML private Label guestNameLabel;
  @FXML private Label addressLabel;
  @FXML private Label contactLabel;
  @FXML private Label roomTypeLabel;
  @FXML private Label checkInLabel;
  @FXML private Label checkOutLabel;
  @FXML private Label messageLabel;

  @FXML
  private void handleSearch() {
    String reservationNo = reservationNoField.getText().trim();

    if (reservationNo.isEmpty()) {
      messageLabel.setText("Error: Reservation number is required");
      return;
    }

    try {
      ApiClient.ReservationResponse res = ApiClient.getReservation(reservationNo);
      guestNameLabel.setText("Guest: " + res.guestName);
      addressLabel.setText("Address: " + res.address);
      contactLabel.setText("Contact: " + res.contactNumber);
      roomTypeLabel.setText("Room Type: " + res.roomType);
      checkInLabel.setText("Check-in: " + res.checkIn);
      checkOutLabel.setText("Check-out: " + res.checkOut);
      messageLabel.setText("Reservation found");
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
    guestNameLabel.setText("Guest: -");
    addressLabel.setText("Address: -");
    contactLabel.setText("Contact: -");
    roomTypeLabel.setText("Room Type: -");
    checkInLabel.setText("Check-in: -");
    checkOutLabel.setText("Check-out: -");
  }
}
