package com.zerobase.reservebase.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private String description;
    private LocalTime openTime;
    private LocalTime closeTime;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;
}


