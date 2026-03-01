package com.oceanview.backend.repo;

import com.oceanview.backend.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReservationRepo extends JpaRepository<Reservation, Long> {
  Optional<Reservation> findByReservationNo(String reservationNo);
  boolean existsByReservationNo(String reservationNo);
}
