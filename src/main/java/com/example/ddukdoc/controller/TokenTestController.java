package com.example.ddukdoc.controller;

import com.example.ddukdoc.entity.Member;
import com.example.ddukdoc.repository.MemberRepository;
import com.example.ddukdoc.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/token")
public class TokenTestController {

    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private MemberRepository memberRepository;

    @GetMapping("/tokenTestPage")
    public String tokenTestPage(){
        return "tokenTestPage";
    }

    @PostMapping("/extend")
    public ResponseEntity<?> extend(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if(token == null || token.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("사용자 인증 정보가 존재하지 않습니다.");
        }

        try{
            String jwt = token.split(" ")[1];
            String username = jwtUtil.getUsername(jwt);
            Member member = memberRepository.findByUsername(username);

            String newToken = "Bearer " + jwtUtil.createJwt(member);

            return ResponseEntity.ok(newToken);
        } catch (ExpiredJwtException e) {
            Claims claims = e.getClaims();
            String username = claims.get("username", String.class);

            Member member = memberRepository.findByUsername(username);
            String newToken = "Bearer " + jwtUtil.createJwt(member);
            return ResponseEntity.ok(newToken);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 유효하지 않습니다.");
        }
    }
}
