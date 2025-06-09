package com.zerobase.reservebase.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class StoreRequest {
    private String name;
    private String location;
    private String description;

    private LocalTime openTime;
    private LocalTime closeTime;
}
