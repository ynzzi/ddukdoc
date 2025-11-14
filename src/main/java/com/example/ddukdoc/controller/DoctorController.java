package com.example.ddukdoc.controller;

import com.example.ddukdoc.entity.Doctor;
import com.example.ddukdoc.entity.Member;
import com.example.ddukdoc.entity.QnA;
import com.example.ddukdoc.repository.DoctorRepository;
import com.example.ddukdoc.repository.MemberRepository;
import com.example.ddukdoc.repository.QnARepository;
import com.example.ddukdoc.utils.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/doc")
public class DoctorController {

    @Autowired
    QnARepository qnARepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    JWTUtil jwtUtil;

    // qna 메인페이지 -> 조회 및 filter
    @GetMapping("/qnaList")
    public String qnaList(Model model, HttpServletRequest request) {
        // 인터셉터에서 담은 현재 로그인한 의사 정보
        Member doctor = (Member) request.getAttribute("doctor");

        if (doctor == null) {
            return "redirect:/login"; // 의사가 아니면 로그인 화면
        }

        // 미답변(QnA.isAnswered = 'N') 중 현재 의사에게 등록된 것
        List<QnA> isPending = qnARepository.findByIsAnsweredAndDoctor("N", doctor);

        // 답변완료(QnA.isAnswered = 'Y') 중 현재 의사에게 등록된 것
        List<QnA> isAnswered = qnARepository.findByIsAnsweredAndDoctor("Y", doctor);

        model.addAttribute("isPending", isPending);
        model.addAttribute("isAnswered", isAnswered);

        return "doctor/qnaList";  // qnaList.html 렌더링
    }





    // 답변 페이지로 이동
    @GetMapping("/goRegForm")
    public String goRegForm(@RequestParam("id") int id, Model model, HttpServletRequest request) {
        Optional<QnA> optionalQnA = qnARepository.findById(id);
        if (optionalQnA.isPresent()) {
            Member doctorMember = (Member) request.getAttribute("doctor");
            Doctor doctor = doctorRepository.findById(doctorMember.getId()).orElseThrow(() -> new IllegalStateException("Doctor not found"));
            model.addAttribute("qna", optionalQnA.get());
            model.addAttribute("doctor", doctor);
            return "doctor/replyForm";
        } else {
            return "redirect:/doc/qnaList";  // 없으면 목록으로 리다이렉트
        }
    }

    @GetMapping("/getQnaDetail")
    @ResponseBody
    public Map<String, Object> getQnaDetail(@RequestParam int id, HttpServletRequest request) {
        Optional<QnA> optionalQnA = qnARepository.findById(id);
        if (!optionalQnA.isPresent()) {
            throw new IllegalArgumentException("No QnA found for id: " + id);
        }
        QnA qna = optionalQnA.get();

        Member doctorMember = (Member) request.getAttribute("doctor");
        Doctor doctor = doctorRepository.findById(doctorMember.getId())
                .orElseThrow(() -> new IllegalStateException("Doctor not found"));

        Map<String, Object> result = new HashMap<>();
        result.put("qnaId", qna.getId());
        result.put("createdAt", qna.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        result.put("writerUsername", qna.getWriter().getUsername());
        result.put("title", qna.getTitle());
        result.put("content", qna.getContent());
        result.put("doctorUsername", doctor.getMember().getUsername());
        result.put("doctorName", doctor.getMember().getName());
        result.put("treatType", doctor.getTreatType());
        result.put("answer", qna.getAnswer());

        return result;
    }



    // 답변
    @PostMapping("/regProc")
    public String regProc(QnA qnA){
        Optional<QnA> optionalQnA = qnARepository.findById(qnA.getId());
        if(optionalQnA.isPresent()){
            QnA qna = optionalQnA.get();
            qna.setAnswer(qnA.getAnswer());
            qna.setIsAnswered("Y");
            qna.setAnsweredAt(LocalDateTime.now());
            qnARepository.save(qna);
            return "redirect:/doc/qnaList";
        }

        return "doctor/replyForm";
    }

    // 메인 페이지로 가기
    @GetMapping("/main")
    public String goMain(){

        return "redirect:/";
    }
}