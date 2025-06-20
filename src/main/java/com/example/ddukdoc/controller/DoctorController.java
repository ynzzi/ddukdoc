package com.example.ddukdoc.controller;

import com.example.ddukdoc.entity.QnA;
import com.example.ddukdoc.repository.QnARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/doc")
public class DoctorController {

    @Autowired
    QnARepository qnARepository;

    // qna 메인페이지 -> 조회 및 filter
    @GetMapping("/qnaList")
    public String qnaList(){
        return "qnaList";
    }

    // 답변
    @PostMapping("/registProc")
    public String registProc(QnA qnA){
        qnA.setIsAnswered("T");
        qnARepository.save(qnA);

        return "";
    }

    // 메인 페이지로 가기
    @GetMapping("/main")
    public String goMain(){

        return "index";
    }
}