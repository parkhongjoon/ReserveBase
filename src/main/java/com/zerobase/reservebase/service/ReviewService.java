package com.zerobase.reservebase.service;

import com.zerobase.reservebase.domain.Reservation;
import com.zerobase.reservebase.domain.Review;
import com.zerobase.reservebase.domain.User;
import com.zerobase.reservebase.dto.ReviewRequest;
import com.zerobase.reservebase.dto.ReviewUpdateRequest;
import com.zerobase.reservebase.repository.ReservationRepository;
import com.zerobase.reservebase.repository.ReviewRepository;
import com.zerobase.reservebase.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    // 작성
    public void writeReview(String email, ReviewRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new RuntimeException("예약 없음"));

        if (!reservation.getCustomer().getId().equals(user.getId())) {
            throw new RuntimeException("본인의 예약이 아닙니다.");
        }

        if (reviewRepository.findByReservation(reservation).isPresent()) {
            throw new RuntimeException("이미 리뷰가 작성되었습니다.");
        }

        Review review = Review.builder()
                .writer(user)
                .store(reservation.getStore())
                .reservation(reservation)
                .content(request.getContent())
                .rating(request.getRating())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        reviewRepository.save(review);
    }

    public void updateReview(String email, Long reviewId, ReviewUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰 없음"));

        if (!review.getWriter().getId().equals(user.getId())) {
            throw new RuntimeException("리뷰 작성자만 수정할 수 있습니다.");
        }

        review.setContent(request.getContent());
        review.setRating(request.getRating());
        review.setUpdatedAt(LocalDateTime.now());

        reviewRepository.save(review);
    }

    public void deleteReview(String email, Long reviewId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰 없음"));

        boolean isWriter = review.getWriter().getId().equals(user.getId());
        boolean isStoreOwner = review.getStore().getOwner().getId().equals(user.getId());

        if (!isWriter && !isStoreOwner) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        reviewRepository.delete(review);
    }
}
