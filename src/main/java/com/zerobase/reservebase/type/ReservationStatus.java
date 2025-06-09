package com.zerobase.reservebase.type;

public enum ReservationStatus {
    PENDING,     // 생성 직후
    APPROVED,    // 점주 승인
    REJECTED,    // 점주 거절
    VISITED,
    CANCELED,
    RESERVED
}
