package com.zerobase.reservebase.controller;

import com.zerobase.reservebase.dto.StoreRequest;
import com.zerobase.reservebase.dto.StoreResponse;
import com.zerobase.reservebase.service.StoreService;
import com.zerobase.reservebase.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    // 매장등록
    public ResponseEntity<String> registerStore(
            @RequestBody StoreRequest request,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.getEmailFromToken(token);
        storeService.registerStore(request, email);
        return ResponseEntity.ok("매장 등록 완료");
    }

    @GetMapping("/stores")
    // 매장목록 확인
    public ResponseEntity<Page<StoreResponse>> getStores(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<StoreResponse> stores = storeService.getStores(name, location, page, size);
        return ResponseEntity.ok(stores);
    }

}
