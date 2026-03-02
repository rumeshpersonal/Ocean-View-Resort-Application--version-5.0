package com.oceanview.backend.service;

import com.oceanview.backend.model.User;
import com.oceanview.backend.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DataSeeder {

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private static boolean dataSeededOnce = false;

  @EventListener(ApplicationReadyEvent.class)
  public void seedData() {
    // Only run once per application instance
    if (dataSeededOnce) {
      return;
    }
    dataSeededOnce = true;

    try {
      String username = "reception";
      String password = "Reception@123";

      // Try to find existing user
      var existingUser = userRepo.findByUsername(username);

      if (existingUser.isPresent()) {
        // User exists - update password to ensure it's correct
        User user = existingUser.get();
        String newHash = passwordEncoder.encode(password);
        user.setPasswordHash(newHash);
        userRepo.save(user);
        System.out.println("✓ User '" + username + "' password updated");
      } else {
        // User doesn't exist - create new one
        User receptionUser = User.builder()
            .username(username)
            .passwordHash(passwordEncoder.encode(password))
            .build();
        userRepo.save(receptionUser);
        System.out.println("✓ User '" + username + "' created with password '" + password + "'");
      }

    } catch (Exception e) {
      System.err.println("✗ Error in DataSeeder: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
