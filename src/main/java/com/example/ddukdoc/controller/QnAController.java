package com.example.ddukdoc.controller;

import com.example.ddukdoc.dto.QnaRequestDTO;
import com.example.ddukdoc.entity.Doctor;
import com.example.ddukdoc.entity.Member;
import com.example.ddukdoc.entity.QnA;
import com.example.ddukdoc.repository.DoctorRepository;
import com.example.ddukdoc.repository.MemberRepository;
import com.example.ddukdoc.repository.QnARepository;
import com.example.ddukdoc.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
public class QnAController {

    @Autowired
    private QnARepository qnaRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @GetMapping("/qna/write")
    public String writePage(@RequestParam("doctorId") Integer doctorId, Model model) {
        model.addAttribute("doctorId", doctorId);
        return "qna_write"; // templates/qna_write.html
    }

    @PostMapping("/api/qna/new")
    @ResponseBody
    public ResponseEntity<?> register(@RequestBody QnaRequestDTO dto,
                                      @RequestHeader("Authorization") String authHeader) {
        // 1. 인증 헤더 확인 (JWT 토큰 확인)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
        }

        try {
            // 2. JWT 토큰에서 사용자 정보 추출
            String token = authHeader.substring(7); // "Bearer " 제거
            String username = jwtUtil.getUsername(token); // JWT에서 username 추출
            Member writer = memberRepository.findByUsername(username);
            if (writer == null) {
                throw new RuntimeException("작성자 정보가 없습니다. username: " + username);
            }


            // 3. 의사 정보 추출
            Member doctorMember = memberRepository.findById(dto.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("의사 정보 없음"));

            Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("Doctor 상세 정보 없음"));

            // 4. QnA 엔티티 생성 및 설정
            QnA qna = new QnA();
            qna.setTitle(dto.getTitle());
            qna.setContent(dto.getContent());
            qna.setIsAnswered("N"); // 미답변 상태
            qna.setWriter(writer);
            qna.setDoctor(doctorMember);

            // 5. DB에 저장
            qnaRepository.save(qna);

            return ResponseEntity.ok(Map.of(
                    "message", "등록 완료",
                    "treatType", doctor.getTreatType()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류 발생: " + e.getMessage());
        }
    }

}
