package com.zerobase.reservebase.controller;

import com.zerobase.reservebase.dto.SignInRequest;
import com.zerobase.reservebase.dto.SignupRequest;
import com.zerobase.reservebase.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 API", description = "회원가입 및 로그인 관련 기능")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody SignInRequest request) {
        String token = authService.signin(request);  // JWT 토큰 반환
        return ResponseEntity.ok(token);
    }
}
