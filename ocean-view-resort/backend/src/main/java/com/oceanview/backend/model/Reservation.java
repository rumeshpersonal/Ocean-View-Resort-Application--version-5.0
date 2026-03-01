package com.oceanview.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name="reservations")
public class Reservation {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="reservation_no", nullable=false, unique=true, length=30)
  private String reservationNo;

  @ManyToOne(optional=false)
  @JoinColumn(name="guest_id")
  private Guest guest;

  @ManyToOne(optional=false)
  @JoinColumn(name="room_type_id")
  private RoomType roomType;

  @Column(name="check_in", nullable=false)
  private LocalDate checkIn;

  @Column(name="check_out", nullable=false)
  private LocalDate checkOut;
}
