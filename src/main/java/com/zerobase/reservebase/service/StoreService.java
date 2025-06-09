package com.zerobase.reservebase.service;

import com.zerobase.reservebase.domain.Store;
import com.zerobase.reservebase.domain.User;
import com.zerobase.reservebase.dto.StoreRequest;
import com.zerobase.reservebase.dto.StoreResponse;
import com.zerobase.reservebase.repository.StoreRepository;
import com.zerobase.reservebase.repository.UserRepository;
import com.zerobase.reservebase.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    // 가게 등록
    public void registerStore(StoreRequest request, String email) {
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        if (!owner.getRole().equals(Role.PARTNER)) {
            throw new RuntimeException("파트너만 등록 가능합니다.");
        }

        Store store = new Store();
        store.setName(request.getName());
        store.setLocation(request.getLocation());
        store.setDescription(request.getDescription());
        store.setOwner(owner);
        store.setOpenTime(request.getOpenTime());
        store.setCloseTime(request.getCloseTime());

        storeRepository.save(store);
    }

    // 조건별 확인
    public Page<StoreResponse> getStores(String name, String location, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Store> stores;

        if (name != null && location != null) {
            stores = storeRepository.findByNameContainingAndLocationContaining(name, location, pageable);
        } else if (name != null) {
            stores = storeRepository.findByNameContaining(name, pageable);
        } else if (location != null) {
            stores = storeRepository.findByLocationContaining(location, pageable);
        } else {
            stores = storeRepository.findAll(pageable);
        }

        return stores.map(StoreResponse::fromEntity);
    }

}

