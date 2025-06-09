package com.zerobase.reservebase.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewUpdateRequest {
    private String content;
    private Integer rating;
}
