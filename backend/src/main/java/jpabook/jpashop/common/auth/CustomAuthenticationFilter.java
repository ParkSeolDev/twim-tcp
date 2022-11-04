package jpabook.jpashop.common.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jpabook.jpashop.api.request.UserLoginPostReq;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final UsernamePasswordAuthenticationToken authRequest;

        final UserLoginPostReq userLoginPostReq;
        try {
            // 사용자 요청 정보로 UserPasswordAuthenticationToken 발급
            userLoginPostReq = new ObjectMapper().readValue(request.getInputStream(), UserLoginPostReq.class);
            System.out.println("userLoginPostReq : " + userLoginPostReq);
            authRequest = new UsernamePasswordAuthenticationToken(userLoginPostReq.getUserId(), userLoginPostReq.getPassword());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Token 발급 실패");
        }
        setDetails(request, authRequest);

        // AuthenticationManager에게 전달 -> AuthenticationProvider의 인증 메서드 실행
        return this.getAuthenticationManager().authenticate(authRequest);
    }

}