package com.oceanview.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/help")
@CrossOrigin("*")
public class HelpController {

  @GetMapping
  public ResponseEntity<?> getHelp() {
    String helpText = """
        ===========================================
        OCEAN VIEW RESORT - HELP GUIDE
        ===========================================
        
        Welcome to Ocean View Resort's Reservation System
        
        AVAILABLE OPERATIONS:
        
        1. CREATE RESERVATION
           - Enter guest details (name, address, contact)
           - Select room type: STANDARD, DELUXE, or SUITE
           - Choose check-in and check-out dates
           - System generates unique reservation number
           
        2. VIEW RESERVATION
           - Enter reservation number (format: RES-2026-XXXX)
           - View guest details and reservation dates
           
        3. GENERATE BILL
           - Enter reservation number
           - Bill calculates: nights × room rate per night
           - Displays total cost
           
        ROOM TYPES & RATES:
        - STANDARD: LKR 12,000 per night
        - DELUXE: LKR 18,000 per night
        - SUITE: LKR 25,000 per night
        
        CONTACT INFORMATION:
        - Must be valid phone number (e.g., 0771234567)
        - Required for all reservations
        
        IMPORTANT:
        - Check-out date must be after check-in date
        - Minimum 1 night stay required
        - All fields are mandatory
        
        For support, contact the reception desk.
        ===========================================
        """;
    return ResponseEntity.ok(Map.of("message", helpText));
  }
}
