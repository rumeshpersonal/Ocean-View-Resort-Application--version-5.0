package com.oceanview.backend.service;

import com.oceanview.backend.model.User;
import com.oceanview.backend.repo.UserRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DataSeeder {

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @PostConstruct
  public void seedData() {
    // Check if admin user exists
    if (userRepo.findByUsername("reception").isEmpty()) {
      User receptionUser = User.builder()
          .username("reception")
          .passwordHash(passwordEncoder.encode("Reception@123"))
          .build();
      userRepo.save(receptionUser);
      System.out.println("✓ Admin user 'reception' created");
    }
  }
}
