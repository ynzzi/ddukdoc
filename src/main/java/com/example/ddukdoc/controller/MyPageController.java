package com.example.ddukdoc.controller;

import com.example.ddukdoc.entity.Member;
import com.example.ddukdoc.entity.QnA;
import com.example.ddukdoc.repository.DoctorRepository;
import com.example.ddukdoc.repository.MemberRepository;
import com.example.ddukdoc.repository.QnARepository;
import com.example.ddukdoc.utils.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MyPageController {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private QnARepository qnaRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JWTUtil jwtUtil;



    @GetMapping("/mypage")
    public String mypage() {
        return "mypage";
    }


    @GetMapping("/api/mypage")
    @ResponseBody
    public ResponseEntity<?> getMypageData(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        // 1. Authorization 헤더 검증
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
        }

        try {
            // 2. "Bearer " 제거 후 토큰 파싱
            String token = authHeader.substring(7).trim(); // 혹시 모를 공백 제거
            String username = jwtUtil.getUsername(token);

            // 3. 사용자 조회
            Member member = memberRepository.findByUsername(username);
            if (member == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }

            // 4. QnA 조회 및 응답 구성
            List<QnA> qnaList = qnaRepository.findAllByWriter(member);

            Map<String, Object> result = new HashMap<>();
            result.put("member", member);
            result.put("qnaList", qnaList);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            // 예외 발생 시 (토큰 오류 등)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }



}
