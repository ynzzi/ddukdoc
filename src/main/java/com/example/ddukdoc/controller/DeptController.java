package com.example.ddukdoc.controller;

import com.example.ddukdoc.entity.Doctor;
import com.example.ddukdoc.entity.QnA;
import com.example.ddukdoc.repository.DoctorRepository;
import com.example.ddukdoc.repository.QnARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    QnARepository qnARepository;


    // 선택 과 의사, 질문 목록 전체 조회 (”/list”)
    @GetMapping("/list")
    public String list(@RequestParam("name") String treatType, Model model) {
        //선택한 진료과 의사들 조회
        List<Doctor> doctorList = doctorRepository.findByTreatType(treatType);

        //선택한 진료과 질문 목록(의사ID 추출)
        List<Integer> doctorIds = doctorList.stream()
                .map(Doctor::getId)
                .toList();

        // 해당 의사들의 질문 목록 조회
        List<QnA> qnaList = qnARepository.findByDoctorIdIn(doctorIds);

        model.addAttribute("treatType", treatType);
        model.addAttribute("doctorList", doctorList);
        model.addAttribute("qnaList", qnaList);

        return "list";
    }

    // 선택한 의사의 정보와 질문 목록 조회 (Ajax용)
    @GetMapping("/doctor")
    @ResponseBody
    public Map<String, Object> getDoctorDetail(@RequestParam("id") Integer doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        List<QnA> qnaList = qnARepository.findByDoctorId(doctorId);

        if (doctor == null) {
            return Collections.emptyMap();
        }

        Map<String, Object> doctorMap = new HashMap<>();
        doctorMap.put("name", doctor.getMember().getName());
        doctorMap.put("hospital", doctor.getHospital());
        doctorMap.put("treatType", doctor.getTreatType());
        doctorMap.put("hTime", doctor.getHTime());

        List<Map<String, Object>> qnaListMap = qnaList.stream().map(qna -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", qna.getId());
            map.put("title", qna.getTitle());
            map.put("isAnswered", qna.getIsAnswered());
            return map;
        }).toList();

        Map<String, Object> result = new HashMap<>();
        result.put("doctor", doctorMap);
        result.put("qnaList", qnaListMap);

        return result;
    }

    // 질문하기 버튼
    // 질문 등록 (”/regist”)


    // 질문 삭제 (”/delete”)
    @PostMapping("/delete")
    public String deleteQna(@RequestParam("id") Integer qnaId) {
        QnA qna = qnARepository.findById(qnaId).orElse(null);
        if (qna != null) {
            qnARepository.delete(qna);
        }
        return "redirect:/";
    }

    // 질문 상세 조회 (”/detail”)
    @GetMapping("/detail")
    public String detail(@RequestParam("id") Integer qnaId, Model model, Principal principal) {
        QnA qna = qnARepository.findById(qnaId).orElse(null);
        if (qna == null) {
            return "redirect:/"; // 글 없으면 메인으로 이동
        }

        boolean isOwner = false;
        if (principal != null && qna.getWriter() != null) {
            isOwner = principal.getName().equals(qna.getWriter().getUsername());
        }

        model.addAttribute("qna", qna);
        model.addAttribute("isOwner", isOwner);

        return "detail";
    }


}
