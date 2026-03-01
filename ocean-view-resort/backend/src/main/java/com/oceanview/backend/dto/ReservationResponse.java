package com.oceanview.backend.dto;

import java.time.LocalDate;

public record ReservationResponse(
  String reservationNo,
  String guestName,
  String address,
  String contactNumber,
  String roomType,
  LocalDate checkIn,
  LocalDate checkOut
) {}
