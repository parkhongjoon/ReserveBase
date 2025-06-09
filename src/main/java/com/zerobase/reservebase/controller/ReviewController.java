package com.zerobase.reservebase.controller;

import com.zerobase.reservebase.dto.ReviewRequest;
import com.zerobase.reservebase.dto.ReviewUpdateRequest;
import com.zerobase.reservebase.service.ReviewService;
import com.zerobase.reservebase.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtUtil jwtUtil;

    @PostMapping
    // 리뷰 작성
    public ResponseEntity<String> writeReview(
            @RequestBody ReviewRequest request,
            @RequestHeader("Authorization") String authHeader) {
        String email = jwtUtil.getEmailFromToken(authHeader.replace("Bearer ", ""));
        reviewService.writeReview(email, request);
        return ResponseEntity.ok("리뷰 작성 완료");
    }

    @PutMapping("/{reviewId}")
    // 수정
    public ResponseEntity<String> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewUpdateRequest request,
            @RequestHeader("Authorization") String authHeader) {
        String email = jwtUtil.getEmailFromToken(authHeader.replace("Bearer ", ""));
        reviewService.updateReview(email, reviewId, request);
        return ResponseEntity.ok("리뷰 수정 완료");
    }

    @DeleteMapping("/{reviewId}")
    // 삭제
    public ResponseEntity<String> deleteReview(
            @PathVariable Long reviewId,
            @RequestHeader("Authorization") String authHeader) {
        String email = jwtUtil.getEmailFromToken(authHeader.replace("Bearer ", ""));
        reviewService.deleteReview(email, reviewId);
        return ResponseEntity.ok("리뷰 삭제 완료");
    }
}
