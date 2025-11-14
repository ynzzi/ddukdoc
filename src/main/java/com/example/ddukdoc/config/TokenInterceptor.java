package com.example.ddukdoc.config;

import com.example.ddukdoc.entity.Member;
import com.example.ddukdoc.repository.MemberRepository;
import com.example.ddukdoc.utils.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // 쿠키에서 토큰 가져오기
        String token = null;
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("token".equals(c.getName())) {
                    token = c.getValue();
                }
            }
        }

        // 토큰 없음 → 로그인
        if (token == null) {
            response.sendRedirect("/login");
            return false;
        }

        try {
            String username = jwtUtil.getUsername(token); // 토큰 파싱
            Member member = memberRepository.findByUsername(username);

            if (member == null || !"DOCTOR".equals(member.getRole())) {
                response.sendRedirect("/"); // 닥터 아니면 메인
                return false;
            }

            request.setAttribute("doctor", member); // 닥터 정보
            return true;

        } catch (Exception e) {
            response.sendRedirect("/login");
            return false;
        }
    }
}
