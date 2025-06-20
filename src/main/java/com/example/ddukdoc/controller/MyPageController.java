package com.example.ddukdoc.controller;

import com.example.ddukdoc.entity.Member;
import com.example.ddukdoc.entity.QnA;
import com.example.ddukdoc.repository.DoctorRepository;
import com.example.ddukdoc.repository.MemberRepository;
import com.example.ddukdoc.repository.QnARepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MyPageController {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private QnARepository qnaRepository;





}
