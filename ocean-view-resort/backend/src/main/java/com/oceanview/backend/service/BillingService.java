package com.oceanview.backend.service;

import com.oceanview.backend.dto.BillResponse;
import com.oceanview.backend.model.Bill;
import com.oceanview.backend.model.Reservation;
import com.oceanview.backend.repo.BillRepo;
import com.oceanview.backend.repo.ReservationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class BillingService {

  @Autowired
  private BillRepo billRepo;

  @Autowired
  private ReservationRepo reservationRepo;

  public BillResponse generateBill(String reservationNo) {

    Reservation reservation = reservationRepo.findByReservationNo(reservationNo)
        .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

    // Calculate nights
    int nights = (int) ChronoUnit.DAYS.between(
        reservation.getCheckIn(),
        reservation.getCheckOut()
    );

    if (nights <= 0) {
      throw new IllegalArgumentException("Invalid reservation dates");
    }

  // Room rate & type
  double ratePerNight = reservation.getRoomType().getRatePerNight();
  String roomTypeName = reservation.getRoomType().getTypeName().name();
  double totalAmount = nights * ratePerNight;

    // Check if bill already exists
    Bill existingBill = billRepo.findByReservation_ReservationNo(reservationNo).orElse(null);

    if (existingBill != null) {
      return new BillResponse(
          reservationNo,
          existingBill.getNights(),
          ratePerNight,
          existingBill.getTotalAmount(),
          roomTypeName
      );
    }

    // Create new bill
    Bill bill = Bill.builder()
        .reservation(reservation)
        .nights(nights)
        .totalAmount(totalAmount)
        .createdAt(LocalDateTime.now())
        .build();

    if (bill != null) {
      billRepo.save(bill);
    }

    return new BillResponse(
        reservationNo,
        nights,
        ratePerNight,
        totalAmount,
        roomTypeName
    );
  }
}
