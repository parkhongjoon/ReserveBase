package com.zerobase.reservebase.controller;

import com.zerobase.reservebase.dto.ReservationRequest;
import com.zerobase.reservebase.dto.ReservationResponse;
import com.zerobase.reservebase.service.ReservationService;
import com.zerobase.reservebase.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final JwtUtil jwtUtil;

    // 예약
    @PostMapping
    public ResponseEntity<String> makeReservation(
            @RequestBody ReservationRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.getEmailFromToken(token);
        reservationService.makeReservation(email, request);
        return ResponseEntity.ok("예약 완료");
    }

    // 점주 예약확인
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<ReservationResponse>> getReservationsByStore(
            @PathVariable Long storeId,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        String partnerEmail = jwtUtil.getEmailFromToken(token);
        return ResponseEntity.ok(reservationService.getReservationsByStore(partnerEmail, storeId));
    }

    // 유저 예약조회
    @GetMapping("/my")
    public ResponseEntity<List<ReservationResponse>> getMyReservations(
            @RequestHeader("Authorization") String authHeader
    ) {
        String email = jwtUtil.getEmailFromToken(authHeader.replace("Bearer ", ""));
        return ResponseEntity.ok(reservationService.getReservationsByUser(email));
    }

    // 방문 확인 처리
    @PostMapping("/visit/{reservationId}")
    public ResponseEntity<String> confirmVisit(
            @PathVariable Long reservationId,
            @RequestHeader("Authorization") String authHeader
    ) {
        String email = jwtUtil.getEmailFromToken(authHeader.replace("Bearer ", ""));
        reservationService.confirmVisit(reservationId, email);
        return ResponseEntity.ok("방문 확인 완료");
    }

    // 예약 취소
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<String> cancelReservation(
            @PathVariable Long reservationId,
            @RequestHeader("Authorization") String authHeader
    ) {
        String email = jwtUtil.getEmailFromToken(authHeader.replace("Bearer ", ""));
        reservationService.cancelReservation(reservationId, email);
        return ResponseEntity.ok("예약 취소 완료");
    }

    // 예약 승인
    @PostMapping("/approve/{reservationId}")
    public ResponseEntity<String> approveReservation(
            @PathVariable Long reservationId,
            @RequestHeader("Authorization") String authHeader
    ) {
        String email = jwtUtil.getEmailFromToken(authHeader.replace("Bearer ", ""));
        reservationService.approveReservation(reservationId, email);
        return ResponseEntity.ok("예약 승인 완료");
    }

    // 예약 거절
    @PostMapping("/reject/{reservationId}")
    public ResponseEntity<String> rejectReservation(
            @PathVariable Long reservationId,
            @RequestHeader("Authorization") String authHeader
    ) {
        String email = jwtUtil.getEmailFromToken(authHeader.replace("Bearer ", ""));
        reservationService.rejectReservation(reservationId, email);
        return ResponseEntity.ok("예약 거절 완료");
    }


}

