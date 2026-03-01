package com.oceanview.backend.repo;

import com.oceanview.backend.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepo extends JpaRepository<Guest, Long> {}
