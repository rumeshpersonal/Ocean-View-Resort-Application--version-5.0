package com.oceanview.backend.service;

import com.oceanview.backend.dto.ReservationCreateRequest;
import com.oceanview.backend.dto.ReservationResponse;
import com.oceanview.backend.model.Guest;
import com.oceanview.backend.model.Reservation;
import com.oceanview.backend.model.RoomType;
import com.oceanview.backend.repo.GuestRepo;
import com.oceanview.backend.repo.ReservationRepo;
import com.oceanview.backend.repo.RoomTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class ReservationService {

  @Autowired
  private ReservationRepo reservationRepo;

  @Autowired
  private GuestRepo guestRepo;

  @Autowired
  private RoomTypeRepo roomTypeRepo;

  public ReservationResponse createReservation(ReservationCreateRequest request) {
    // Validate dates
    if (request.checkOut().isBefore(request.checkIn()) || request.checkOut().isEqual(request.checkIn())) {
      throw new IllegalArgumentException("Check-out date must be after check-in date");
    }

    // Validate room type
    RoomType.RoomTypeName roomTypeName;
    try {
      roomTypeName = RoomType.RoomTypeName.valueOf(request.roomType());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid room type. Must be STANDARD, DELUXE, or SUITE");
    }

    RoomType roomType = roomTypeRepo.findByTypeName(roomTypeName)
        .orElseThrow(() -> new IllegalArgumentException("Room type not found in database"));

    // Create guest
    Guest guest = Guest.builder()
        .fullName(request.guestName())
        .address(request.address())
        .contactNumber(request.contactNumber())
        .build();
    guestRepo.save(guest);

    // Generate unique reservation number
    String reservationNo;
    do {
      reservationNo = "RES-" + LocalDate.now().getYear() + "-" + String.format("%04d", (int)(Math.random() * 10000));
    } while (reservationRepo.existsByReservationNo(reservationNo));

    // Create reservation
    Reservation reservation = Reservation.builder()
        .reservationNo(reservationNo)
        .guest(guest)
        .roomType(roomType)
        .checkIn(request.checkIn())
        .checkOut(request.checkOut())
        .build();
    reservationRepo.save(reservation);

    return new ReservationResponse(
        reservation.getReservationNo(),
        guest.getFullName(),
        guest.getAddress(),
        guest.getContactNumber(),
        roomType.getTypeName().toString(),
        reservation.getCheckIn(),
        reservation.getCheckOut()
    );
  }

  public ReservationResponse viewReservation(String reservationNo) {
    Reservation reservation = reservationRepo.findByReservationNo(reservationNo)
        .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

    return new ReservationResponse(
        reservation.getReservationNo(),
        reservation.getGuest().getFullName(),
        reservation.getGuest().getAddress(),
        reservation.getGuest().getContactNumber(),
        reservation.getRoomType().getTypeName().toString(),
        reservation.getCheckIn(),
        reservation.getCheckOut()
    );
  }
}
