package com.zerobase.reservebase.repository;

import com.zerobase.reservebase.domain.Reservation;
import com.zerobase.reservebase.domain.Store;
import com.zerobase.reservebase.domain.User;
import com.zerobase.reservebase.type.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStore(Store store);

    List<Reservation> findByCustomer(User customer);

    // 예약 중복방지
    boolean existsByStoreAndReservationTime(Store store, LocalDateTime reservationTime);

    List<Reservation> findByStoreAndStatusAndReservationTimeBetween(
            Store store,
            ReservationStatus status,
            LocalDateTime start,
            LocalDateTime end
    );


}
