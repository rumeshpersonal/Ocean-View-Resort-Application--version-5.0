package com.oceanview.backend.dto;

public record LoginResponse(
  String token,
  String username
) {}
