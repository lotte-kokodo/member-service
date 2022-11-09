package shop.kokodo.memberservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import shop.kokodo.memberservice.dto.MemberDto;
import shop.kokodo.memberservice.service.MemberService;
import shop.kokodo.memberservice.vo.Request.RequestLogin;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import shop.kokodo.memberservice.vo.Response.ResponseLogin;

@Component
@Slf4j
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtTokenCreator jwtTokenCreator;

    private final ObjectMapper objectMapper;


    public LoginAuthenticationFilter(AuthenticationManager authenticationManager,
        JwtTokenCreator jwtTokenCreator,
        ObjectMapper objectMapper) {
        super.setAuthenticationManager(authenticationManager);
        this.objectMapper = objectMapper;
        this.jwtTokenCreator = jwtTokenCreator;
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
                            creds.getPassword()
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
                                            Authentication authResult) throws IOException {

        UserDetailsImpl userDetails = ( ((UserDetailsImpl) authResult.getPrincipal()));
        Long memberId = userDetails.getId();

        //JWT Token 생성
        /*
            1. header에 들어갈 내용 및 서명을 위한 SECRET_KEY (application.yml에 정의)
            2. payload에 들어갈 내용 (sub, exp)
         */
        String accessToken = jwtTokenCreator.generateAccessToken(memberId);
        String refreshToken = jwtTokenCreator.generateRefreshToken(memberId);

        // 바디에 토큰 및 회원 ID 저장.
        ResponseLogin respLogin = new ResponseLogin(memberId, accessToken, refreshToken);
        response.getWriter().write(objectMapper.writeValueAsString(respLogin));
    }
}
