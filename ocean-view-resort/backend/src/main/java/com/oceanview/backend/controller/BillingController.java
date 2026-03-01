package com.oceanview.backend.controller;

import com.oceanview.backend.dto.*;
import com.oceanview.backend.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin("*")
public class BillingController {

  @Autowired
  private BillingService billingService;

  @PostMapping("/{reservationNo}")
  public ResponseEntity<?> generateBill(@PathVariable String reservationNo) {
    try {
      BillResponse response = billingService.generateBill(reservationNo);
      return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage()));
    }
  }
}
