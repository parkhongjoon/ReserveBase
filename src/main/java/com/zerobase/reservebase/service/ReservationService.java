package com.zerobase.reservebase.service;

import com.zerobase.reservebase.domain.Reservation;
import com.zerobase.reservebase.domain.Store;
import com.zerobase.reservebase.domain.User;
import com.zerobase.reservebase.dto.ReservationRequest;
import com.zerobase.reservebase.dto.ReservationResponse;
import com.zerobase.reservebase.repository.ReservationRepository;
import com.zerobase.reservebase.repository.StoreRepository;
import com.zerobase.reservebase.repository.UserRepository;
import com.zerobase.reservebase.type.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    // 예약
    public void makeReservation(String email, ReservationRequest request) {
        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new RuntimeException("매장 없음"));

        // 1. 예약 시간 중복 체크
        boolean exists = reservationRepository.existsByStoreAndReservationTime(store, request.getReservationTime());
        if (exists) {
            throw new RuntimeException("이미 해당 시간에 예약이 존재합니다.");
        }

        // 2. 매장 운영 시간 체크
        LocalTime reservationTimeOnly = request.getReservationTime().toLocalTime();
        if (reservationTimeOnly.isBefore(store.getOpenTime()) || reservationTimeOnly.isAfter(store.getCloseTime())) {
            throw new RuntimeException("매장 운영 시간 외에는 예약할 수 없습니다.");
        }

        // 3. 예약 저장
        Reservation reservation = Reservation.builder()
                .reservationTime(request.getReservationTime())
                .store(store)
                .customer(customer)
                .status(ReservationStatus.PENDING)
                .build();

        reservationRepository.save(reservation);
    }

    // 점주가 예약확인
    public List<ReservationResponse> getReservationsByStore(String partnerEmail, Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("매장 없음"));

        if (!store.getOwner().getEmail().equals(partnerEmail)) {
            throw new RuntimeException("해당 매장에 접근 권한이 없습니다.");
        }

        return reservationRepository.findByStore(store)
                .stream()
                .map(ReservationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 고객 예약확인
    public List<ReservationResponse> getReservationsByUser(String email) {
        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        return reservationRepository.findByCustomer(customer)
                .stream()
                .map(ReservationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 예약 10분전 확인사항
    public void confirmVisit(Long reservationId, String email) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("예약 없음"));

        // 본인 확인
        if (!reservation.getCustomer().getEmail().equals(email)) {
            throw new RuntimeException("본인만 방문 확인이 가능합니다.");
        }

        // 예약 상태 확인
        if (reservation.getStatus() != ReservationStatus.APPROVED) {
            throw new RuntimeException("승인된 예약만 방문 확인 가능합니다.");
        }

        // 현재 시간이 예약 시간 10분 전보다 이후인 경우 예외
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(reservation.getReservationTime().minusMinutes(10))) {
            throw new RuntimeException("방문 확인은 예약 10분 전까지만 가능합니다.");
        }

        reservation.setStatus(ReservationStatus.VISITED);
        reservationRepository.save(reservation);
    }

    // 취소
    public void cancelReservation(Long reservationId, String email) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("예약 없음"));

        if (!reservation.getCustomer().getEmail().equals(email)) {
            throw new RuntimeException("본인 예약만 취소 가능");
        }

        reservation.setStatus(ReservationStatus.CANCELED);
        reservationRepository.save(reservation);
    }

    // 예약 승인
    public void approveReservation(Long reservationId, String partnerEmail) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("예약 없음"));
        if (!reservation.getStore().getOwner().getEmail().equals(partnerEmail)) {
            throw new RuntimeException("접근 권한 없음");
        }
        reservation.setStatus(ReservationStatus.APPROVED);
        reservationRepository.save(reservation);
    }

    // 예약 거절
    public void rejectReservation(Long reservationId, String partnerEmail) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("예약 없음"));
        if (!reservation.getStore().getOwner().getEmail().equals(partnerEmail)) {
            throw new RuntimeException("접근 권한 없음");
        }
        reservation.setStatus(ReservationStatus.REJECTED);
        reservationRepository.save(reservation);
    }

    // 예약 조건 확인
    public List<ReservationResponse> filterReservationsByStore(
            String email,
            Long storeId,
            ReservationStatus status,
            LocalDateTime start,
            LocalDateTime end
    ) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("매장 없음"));

        if (!store.getOwner().getEmail().equals(email)) {
            throw new RuntimeException("접근 권한 없음");
        }

        return reservationRepository.findByStoreAndStatusAndReservationTimeBetween(store, status, start, end)
                .stream()
                .map(ReservationResponse::fromEntity)
                .collect(Collectors.toList());
    }


}

