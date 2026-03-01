package com.oceanview.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name="room_types")
public class RoomType {

  public enum RoomTypeName { STANDARD, DELUXE, SUITE }

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name="type_name", nullable=false, unique=true)
  private RoomTypeName typeName;

  @Column(name="rate_per_night", nullable=false)
  private double ratePerNight;
}
