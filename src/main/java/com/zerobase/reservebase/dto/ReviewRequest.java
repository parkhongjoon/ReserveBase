package com.zerobase.reservebase.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {
    private Long reservationId;
    private String content;
    private Integer rating;
}
