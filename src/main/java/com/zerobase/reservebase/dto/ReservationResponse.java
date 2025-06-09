package com.zerobase.reservebase.dto;

import com.zerobase.reservebase.domain.Reservation;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ReservationResponse {
    private Long id;
    private String storeName;
    private String customerEmail;
    private LocalDateTime reservationTime;

    public static ReservationResponse fromEntity(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .storeName(reservation.getStore().getName())
                .customerEmail(reservation.getCustomer().getEmail())
                .reservationTime(reservation.getReservationTime())
                .build();
    }
}
