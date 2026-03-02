package com.oceanview.desktop.controller;

import com.oceanview.desktop.api.ApiClient;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GenerateBillController {

  @FXML private TextField reservationNoField;
  @FXML private Label nightsLabel;
  @FXML private Label rateLabel;
  @FXML private Label totalLabel;
  @FXML private Label messageLabel;
@FXML private Label roomTypeLabel;
  // ✅ Snapshot area (the VBox in FXML)
  @FXML private VBox billPane;

  // ✅ Download button
  @FXML private Button downloadBtn;

  // Track whether a bill was generated successfully
  private boolean billReady = false;

  @FXML
  private void handleGenerateBill() {
    String reservationNo = reservationNoField.getText().trim();

    if (reservationNo.isEmpty()) {
      messageLabel.setText("Error: Reservation number is required");
      clearLabels();
      billReady = false;
      downloadBtn.setDisable(true);
      return;
    }

    try {
      ApiClient.BillResponse bill = ApiClient.generateBill(reservationNo);

      nightsLabel.setText("Nights: " + bill.nights);
      roomTypeLabel.setText(bill.roomType);
      rateLabel.setText("Rate/Night: Rs. " + String.format("%.2f", bill.ratePerNight));
      totalLabel.setText("Total Amount: Rs. " + String.format("%.2f", bill.totalAmount));

      messageLabel.setText("Bill generated successfully");
      
      billReady = true;
      downloadBtn.setDisable(false);

    } catch (Exception e) {
      messageLabel.setText("Error: " + e.getMessage());
      clearLabels();
      billReady = false;
      downloadBtn.setDisable(true);
    }
  }

  @FXML
  private void handleDownload() {
    if (!billReady) {
      messageLabel.setText("Error: Generate the bill first");
      return;
    }

    Stage stage = (Stage) reservationNoField.getScene().getWindow();

    FileChooser chooser = new FileChooser();
    chooser.setTitle("Save Bill as Image");
    chooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("PNG Image (*.png)", "*.png")
    );

    String resNo = reservationNoField.getText().trim();
    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    chooser.setInitialFileName("Bill_" + resNo.replaceAll("[^a-zA-Z0-9-_]", "_") + "_" + timestamp + ".png");

    File file = chooser.showSaveDialog(stage);
    if (file == null) return;

    try {
      // Snapshot the bill UI area
      WritableImage image = billPane.snapshot(new SnapshotParameters(), null);

      ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);

      messageLabel.setText("Downloaded: " + file.getName());
    } catch (Exception e) {
      messageLabel.setText("Error: Failed to download bill - " + e.getMessage());
    }
  }

  @FXML
  private void handleCancel() {
    Stage stage = (Stage) reservationNoField.getScene().getWindow();
    stage.close();
  }

  private void clearLabels() {
    roomTypeLabel.setText("-");
    nightsLabel.setText("-");
    rateLabel.setText("-");
    totalLabel.setText("-");
  }
}