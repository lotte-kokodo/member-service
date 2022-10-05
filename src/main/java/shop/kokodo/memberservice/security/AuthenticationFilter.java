package shop.kokodo.memberservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import shop.kokodo.memberservice.dto.MemberDto;
import shop.kokodo.memberservice.service.MemberService;
import shop.kokodo.memberservice.vo.Request.RequestLogin;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private MemberService memberService;
    private Environment env;

    public AuthenticationFilter(AuthenticationManager authenticationManager, MemberService memberService, Environment env) {
        super.setAuthenticationManager(authenticationManager);
        this.memberService = memberService;
        this.env = env;
    }

    //인증수행 로직
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try{
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getLoginId(),
                            creds.getPassword(),
                            new ArrayList<>()
                    )
            );
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    //인증 성공 로직
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        String loginId = ( ((User)authResult.getPrincipal()).getUsername() );
        MemberDto memberDto = memberService.getMemberByLoginId(loginId);

        //JWT Token 생성
        /*
            1. header에 들어갈 내용 및 서명을 위한 SECRET_KEY (application.yml에 정의)
            2. payload에 들어갈 내용 (sub, exp)
         */
        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                .setSubject(memberDto.getLoginId())
                .setExpiration(new Date(System.currentTimeMillis() +
                        Long.parseLong(env.getProperty("token.expiration_time"))))
                .compact();

        // 헤더에 토큰 및 ID 저장.
        response.addHeader("token", token);
        response.addHeader("memberId", memberDto.getId().toString());
    }
}
