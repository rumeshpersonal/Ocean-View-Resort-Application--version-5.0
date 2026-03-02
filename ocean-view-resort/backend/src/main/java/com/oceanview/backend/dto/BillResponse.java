package com.oceanview.backend.dto;

public class BillResponse {

  public String reservationNo;
  public int nights;
  public double ratePerNight;
  public double totalAmount;
  public String roomType;

  public BillResponse(String reservationNo, int nights, double ratePerNight,
                      double totalAmount, String roomType) {
    this.reservationNo = reservationNo;
    this.nights = nights;
    this.ratePerNight = ratePerNight;
    this.totalAmount = totalAmount;
    this.roomType = roomType;
  }
}