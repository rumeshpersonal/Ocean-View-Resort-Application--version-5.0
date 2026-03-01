package com.oceanview.backend.controller;

import com.oceanview.backend.dto.*;
import com.oceanview.backend.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin("*")
public class ReservationController {

  @Autowired
  private ReservationService reservationService;

  @PostMapping
  public ResponseEntity<?> createReservation(@Valid @RequestBody ReservationCreateRequest request) {
    try {
      ReservationResponse response = reservationService.createReservation(request);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(400).body(new ErrorResponse(e.getMessage()));
    }
  }

  @GetMapping("/{reservationNo}")
  public ResponseEntity<?> viewReservation(@PathVariable String reservationNo) {
    try {
      ReservationResponse response = reservationService.viewReservation(reservationNo);
      return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage()));
    }
  }
}
