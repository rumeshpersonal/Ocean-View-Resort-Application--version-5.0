package com.oceanview.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name="guests")
public class Guest {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="full_name", nullable=false, length=100)
  private String fullName;

  @Column(nullable=false, length=200)
  private String address;

  @Column(name="contact_number", nullable=false, length=15)
  private String contactNumber;
}
