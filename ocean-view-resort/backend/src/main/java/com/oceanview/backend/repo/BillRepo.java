package com.oceanview.backend.repo;

import com.oceanview.backend.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BillRepo extends JpaRepository<Bill, Long> {
  Optional<Bill> findByReservation_ReservationNo(String reservationNo);
}
