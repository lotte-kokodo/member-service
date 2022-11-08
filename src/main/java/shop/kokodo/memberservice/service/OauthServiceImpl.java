package shop.kokodo.memberservice.service;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import shop.kokodo.memberservice.dto.NaverCallbackParam;
import shop.kokodo.memberservice.dto.NaverMemberInfoRequest;
import shop.kokodo.memberservice.dto.NaverMemberInfoResponse;
import shop.kokodo.memberservice.dto.NaverTokenRequest;
import shop.kokodo.memberservice.dto.NaverTokenResponse;
import shop.kokodo.memberservice.entity.Member;
import shop.kokodo.memberservice.repository.MemberRepository;
import shop.kokodo.memberservice.security.JwtTokenCreator;
import shop.kokodo.memberservice.vo.Response.ResponseLogin;

@Service
@Slf4j
public class OauthServiceImpl implements OauthService {

    private final RestTemplate restTemplate;

    private final NaverTokenRequest naverTokenRequest;

    private final NaverMemberInfoRequest naverMemberInfoRequest;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final MemberRepository memberRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenCreator jwtTokenCreator;

    public OauthServiceImpl(RestTemplate restTemplate,
        NaverTokenRequest naverTokenRequest,
        NaverMemberInfoRequest naverMemberInfoRequest,
        BCryptPasswordEncoder bCryptPasswordEncoder,
        MemberRepository memberRepository,
        AuthenticationManager authenticationManager,
        JwtTokenCreator jwtTokenCreator) {
        this.restTemplate = restTemplate;
        this.naverTokenRequest = naverTokenRequest;
        this.naverMemberInfoRequest = naverMemberInfoRequest;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.memberRepository = memberRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenCreator = jwtTokenCreator;
    }

    @Override
    public ResponseLogin authenticateWithNaver(NaverCallbackParam param) {
        String tokenUrl = naverTokenRequest.getNaverTokenUrl(param.getCode(), param.getState());

        // Naver 사용자 정보를 사용하기 위한 AccessToken 요청
        HttpEntity<NaverTokenRequest> tokenReq = new HttpEntity<>(naverTokenRequest);
        ResponseEntity<NaverTokenResponse> tokenRespEntity = restTemplate.exchange(tokenUrl, HttpMethod.POST, tokenReq, NaverTokenResponse.class);
        NaverTokenResponse tokens = tokenRespEntity.getBody();

        // 응답받은 AccessToken 을 사용해 사용자 정보 취득
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + tokens.getAccess_token());

        HttpEntity<MultiValueMap<String, String>> memberInfoReq = new HttpEntity<>(headers);
        ResponseEntity<NaverMemberInfoResponse> memberInfoRespEntity = restTemplate.exchange(naverMemberInfoRequest.getMeUrl(), HttpMethod.POST, memberInfoReq, NaverMemberInfoResponse.class);
        NaverMemberInfoResponse.Response naverMemberInfo = Objects.requireNonNull(memberInfoRespEntity.getBody()).getResponse();

        // 회원인지 확인
        String loginId = naverMemberInfo.getId();
        String password = loginId + "ADMIN_TOKEN";
        Member memberByOauth = memberRepository.findByLoginId(loginId);

        // 등록된 회원이 아닐 경우 회원가입 진행
        if (memberByOauth == null) {
            String encryptedPassword = bCryptPasswordEncoder.encode(password);
            String birth = String.format("%s-%s", naverMemberInfo.getBirthyear(), naverMemberInfo.getBirthday());

            memberByOauth = Member.create(loginId, naverMemberInfo.getName(), naverMemberInfo.getEmail(),
                password, birth, naverMemberInfo.getProfile_image(), naverMemberInfo.getMobile(),
                encryptedPassword);

            // 회원정보 저장
            memberRepository.save(memberByOauth);
        }
        // 인증(로그인) 처리
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginId, password));

        Long memberId = memberByOauth.getId();
        String accessToken = jwtTokenCreator.generateAccessToken(memberId);
        String refreshToken = jwtTokenCreator.generateRefreshToken(memberId);

        // 바디에 토큰 및 회원 ID 저장.
        return new ResponseLogin(memberByOauth.getId(), accessToken, refreshToken);
    }

}
