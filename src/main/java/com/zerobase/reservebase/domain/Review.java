package com.zerobase.reservebase.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User writer;

    @ManyToOne
    private Store store;

    @OneToOne
    private Reservation reservation;

    private String content;

    private Integer rating;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
