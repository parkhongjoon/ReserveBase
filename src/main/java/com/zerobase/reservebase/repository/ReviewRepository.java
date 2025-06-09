package com.zerobase.reservebase.repository;

import com.zerobase.reservebase.domain.Reservation;
import com.zerobase.reservebase.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByReservation(Reservation reservation);
}
