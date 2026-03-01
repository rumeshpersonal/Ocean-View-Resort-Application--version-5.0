package com.oceanview.backend.service;

import com.oceanview.backend.model.User;
import com.oceanview.backend.repo.UserRepo;
import com.oceanview.backend.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private TokenService tokenService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public String login(String username, String password) {
    User user = userRepo.findByUsername(username)
        .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

    if (!passwordEncoder.matches(password, user.getPasswordHash())) {
      throw new IllegalArgumentException("Invalid credentials");
    }

    return tokenService.generateToken(username);
  }
}
