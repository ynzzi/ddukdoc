package com.example.ddukdoc.controller;

import com.example.ddukdoc.dto.LoginUser;
import com.example.ddukdoc.entity.Member;
import com.example.ddukdoc.repository.MemberRepository;
import com.example.ddukdoc.utils.JWTUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JWTUtil jwtUtil;

    @GetMapping("")
    public String loginForm(){
        return "loginForm";
    }

    @PostMapping("/loginReg")
    public ResponseEntity<?> loginReg(@RequestBody LoginUser loginUser) {
        Member loginMember = memberRepository.findByUsernameAndPassword(
                loginUser.getUsername(),
                loginUser.getPassword()
        );

        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("아이디 또는 비밀번호를 확인하세요.");
        }

        // 토큰 생성
        String jwt = jwtUtil.createJwt(loginMember);

        // 쿠키 심기
        ResponseCookie cookie = ResponseCookie.from("token", jwt)
                .path("/")
                .httpOnly(true)
                .build();

        Map<String, Object> body = Map.of(
                "role", loginMember.getRole()
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(body);
    }

    private String makeToken(Member member){
        String token = "Bearer " +  jwtUtil.createJwt(member);

        return token;
    }
}
