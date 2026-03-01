package com.oceanview.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name="bills")
public class Bill {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(optional=false)
  @JoinColumn(name="reservation_id", unique=true)
  private Reservation reservation;

  @Column(nullable=false)
  private int nights;

  @Column(name="total_amount", nullable=false)
  private double totalAmount;

  @Column(name="created_at", nullable=false)
  private LocalDateTime createdAt;
}
