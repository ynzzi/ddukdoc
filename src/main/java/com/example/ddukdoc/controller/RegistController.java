package com.example.ddukdoc.controller;

import com.example.ddukdoc.entity.Doctor;
import com.example.ddukdoc.entity.Member;
import com.example.ddukdoc.repository.DoctorRepository;
import com.example.ddukdoc.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/regist")
public class RegistController {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping("")
    public String regist() {
        return "regist";
    }

    @GetMapping("/memberRegistForm")
    public String memberRegistForm() {
        return "memberRegistForm";
    }

    @GetMapping("/doctorRegistForm")
    public String doctorRegistForm() {
        return "doctorRegistForm";
    }

    @PostMapping("/memberRegist")
    public String memberResist(Member member) {

        member.setRole("USER");
        memberRepository.save(member);

        return "loginForm";
    }

    @PostMapping("/doctorRegist")
    public String doctorResist(Member member, Doctor doctor) {

        member.setRole("DOCTOR");
        memberRepository.save(member);

        doctor.setMember(member);
        doctorRepository.save(doctor);

        return "loginForm";
    }
}
