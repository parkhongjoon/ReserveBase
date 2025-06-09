package com.zerobase.reservebase.repository;

import com.zerobase.reservebase.domain.Store;
import com.zerobase.reservebase.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    // 기존 메서드
    List<Store> findByOwner(User owner);

    // 이름만으로 검색
    Page<Store> findByNameContaining(String name, Pageable pageable);

    // 지역(location)만으로 검색
    Page<Store> findByLocationContaining(String location, Pageable pageable);

    // 이름 + 지역으로 검색
    Page<Store> findByNameContainingAndLocationContaining(String name, String location, Pageable pageable);
}
