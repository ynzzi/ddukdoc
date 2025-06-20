package com.example.ddukdoc.controller;

import com.example.ddukdoc.entity.QnA;
import com.example.ddukdoc.repository.QnARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/doc")
public class DoctorController {

    @Autowired
    QnARepository qnARepository;

    // qna 메인페이지 -> 조회 및 filter
    @GetMapping("/qnaList")
    public String qnaList(Model model){
        List<QnA> isAnswered = qnARepository.findByIsAnswered("N");
        List<QnA> isPending = qnARepository.findByIsAnswered("Y");
        model.addAttribute("isAnswered", isAnswered);
        model.addAttribute("isPending", isPending);
        return "doctor/qnaList";
    }

    // 답변 페이지로 이동
    @GetMapping("/goRegForm")
    public String goRegForm(@RequestParam("id") int id, Model model) {
        Optional<QnA> optionalQnA = qnARepository.findById(id);
        if (optionalQnA.isPresent()) {
            model.addAttribute("qna", optionalQnA.get());
            return "doctor/replyForm";
        } else {
            return "redirect:/doc/qnaList";  // 없으면 목록으로 리다이렉트
        }
    }


//    // 답변
//    @PostMapping("/regProc")
//    public String regProc(QnA qnA){
//        Optional<QnA> qnA = qnARepository.findById(id);
//        if(qnA.isPresent()){
//            QnA qna = qnA.get();
//            qna.setIsAnswered("T");
//            qnARepository.save(qna);
//            return "doctor/qnaList";
//        }
//
//        return "doctor/replyForm";
//    }

    // 메인 페이지로 가기
    @GetMapping("/main")
    public String goMain(){

        return "index";
    }
}