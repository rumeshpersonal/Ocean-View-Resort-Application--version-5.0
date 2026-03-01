package com.oceanview.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationCreateRequest(
  @NotBlank String guestName,
  @NotBlank String address,
  @NotBlank String contactNumber,
  @NotNull String roomType,
  @NotNull LocalDate checkIn,
  @NotNull LocalDate checkOut
) {}
