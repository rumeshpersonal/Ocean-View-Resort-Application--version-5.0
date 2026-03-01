package com.oceanview.desktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import com.oceanview.desktop.api.ApiClient;
import com.oceanview.desktop.util.Validator;

public class CreateReservationController {
  @FXML private TextField guestNameField;
  @FXML private TextField addressField;
  @FXML private TextField contactField;
  @FXML private ComboBox<String> roomTypeCombo;
  @FXML private TextField checkInField;
  @FXML private TextField checkOutField;
  @FXML private Label messageLabel;

  @FXML
  public void initialize() {
    roomTypeCombo.getItems().addAll("STANDARD", "DELUXE", "SUITE");
    roomTypeCombo.setValue("STANDARD");
  }

  @FXML
  private void handleCreate() {
    // Validate inputs
    String guestName = guestNameField.getText().trim();
    String address = addressField.getText().trim();
    String contact = contactField.getText().trim();
    String roomType = roomTypeCombo.getValue();
    String checkIn = checkInField.getText().trim();
    String checkOut = checkOutField.getText().trim();

    if (!Validator.isNotEmpty(guestName)) {
      messageLabel.setText("Error: Guest name is required");
      return;
    }
    if (!Validator.isNotEmpty(address)) {
      messageLabel.setText("Error: Address is required");
      return;
    }
    if (!Validator.isValidContact(contact)) {
      messageLabel.setText("Error: Invalid contact. Use 0771234567 or +94771234567");
      return;
    }
    if (!Validator.isValidRoomType(roomType)) {
      messageLabel.setText("Error: Invalid room type");
      return;
    }
    if (!Validator.isValidDateRange(checkIn, checkOut)) {
      messageLabel.setText("Error: Check-out must be after check-in (YYYY-MM-DD)");
      return;
    }

    try {
      String reservationNo = ApiClient.createReservation(guestName, address, contact, roomType, checkIn, checkOut);
      messageLabel.setText("Success! Reservation: " + reservationNo);
      clearFields();
    } catch (Exception e) {
      messageLabel.setText("Error: " + e.getMessage());
    }
  }

  @FXML
  private void handleCancel() {
    Stage stage = (Stage) guestNameField.getScene().getWindow();
    stage.close();
  }

  private void clearFields() {
    guestNameField.clear();
    addressField.clear();
    contactField.clear();
    checkInField.clear();
    checkOutField.clear();
  }
}
