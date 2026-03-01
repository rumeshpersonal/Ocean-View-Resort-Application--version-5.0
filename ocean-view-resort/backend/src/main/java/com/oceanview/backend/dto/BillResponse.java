package com.oceanview.backend.dto;

public record BillResponse(
  String reservationNo,
  int nights,
  double ratePerNight,
  double totalAmount
) {}
