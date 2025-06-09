package com.zerobase.reservebase.service;

import com.zerobase.reservebase.domain.User;
import com.zerobase.reservebase.dto.SignInRequest;
import com.zerobase.reservebase.dto.SignupRequest;
import com.zerobase.reservebase.repository.UserRepository;
import com.zerobase.reservebase.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 회원가입
    public void signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role(request.getRole())
                .build();

        userRepository.save(user);

        System.out.println(String.format("회원가입 완료 - 이름: %s | 이메일: %s", user.getName(), user.getEmail()));
    }

    // 로그인
    public String signin(SignInRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("해당 이메일이 존재하지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        System.out.println("로그인 성공 - 사용자: " + user.getName() + ", 이메일: " + user.getEmail());

        // 토큰 생성
        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
    }
}
