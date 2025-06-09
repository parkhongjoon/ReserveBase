package com.zerobase.reservebase.domain;

import com.zerobase.reservebase.type.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime reservationTime;

    @ManyToOne
    private Store store;

    @ManyToOne
    private User customer;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

}
