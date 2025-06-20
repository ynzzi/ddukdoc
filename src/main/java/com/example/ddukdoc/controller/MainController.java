package com.example.ddukdoc.controller;

import com.example.ddukdoc.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    DoctorRepository doctorRepository;

    @GetMapping("/")
    public String root(Model model){
        //진료과 이름 조회(목록)
        List<String> treatType = doctorRepository.findDistinctTreatTypes();
        System.out.println("!!!!!!!treatType = " + treatType); // 디버깅
        model.addAttribute("treatType", treatType);
        return "index";
    }


}
