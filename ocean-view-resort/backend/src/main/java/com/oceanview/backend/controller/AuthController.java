package com.oceanview.backend.controller;

import com.oceanview.backend.dto.*;
import com.oceanview.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
    try {
      String token = authService.login(request.username(), request.password());
      return ResponseEntity.ok(new LoginResponse(token, request.username()));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(401).body(new ErrorResponse(e.getMessage()));
    }
  }
}
