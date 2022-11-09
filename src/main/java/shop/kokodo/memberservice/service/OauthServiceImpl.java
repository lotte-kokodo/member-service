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
import shop.kokodo.memberservice.dto.oauth.KakaoMemberResponse;
import shop.kokodo.memberservice.dto.oauth.KakaoMemberResponse.KakaoAccount;
import shop.kokodo.memberservice.dto.oauth.KakaoMemberResponse.KakaoAccount.Profile;
import shop.kokodo.memberservice.dto.oauth.KakaoOAuthProperty;
import shop.kokodo.memberservice.dto.oauth.KakaoTokenResponse;
import shop.kokodo.memberservice.dto.oauth.OAuthCallbackParam;
import shop.kokodo.memberservice.dto.NaverMemberInfoResponse;
import shop.kokodo.memberservice.dto.oauth.NaverOAuthProperty;
import shop.kokodo.memberservice.dto.oauth.NaverTokenResponse;
import shop.kokodo.memberservice.entity.Member;
import shop.kokodo.memberservice.repository.MemberRepository;
import shop.kokodo.memberservice.security.JwtTokenCreator;
import shop.kokodo.memberservice.vo.Response.ResponseLogin;

@Service
@Slf4j
public class OauthServiceImpl implements OauthService {

    private final RestTemplate restTemplate;

    private final NaverOAuthProperty naverOAuthProperty;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final KakaoOAuthProperty kakaoOAuthProperty;

    private final MemberRepository memberRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenCreator jwtTokenCreator;

    public OauthServiceImpl(RestTemplate restTemplate,
        NaverOAuthProperty naverOAuthProperty,
        BCryptPasswordEncoder bCryptPasswordEncoder,
        KakaoOAuthProperty kakaoOAuthProperty,
        MemberRepository memberRepository,
        AuthenticationManager authenticationManager,
        JwtTokenCreator jwtTokenCreator) {
        this.restTemplate = restTemplate;
        this.naverOAuthProperty = naverOAuthProperty;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.kakaoOAuthProperty = kakaoOAuthProperty;
        this.memberRepository = memberRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenCreator = jwtTokenCreator;
    }

    @Override
    public ResponseLogin authenticateWithNaver(OAuthCallbackParam param) {
        String tokenUrl = naverOAuthProperty.getNaverTokenUrl(param.getCode(), param.getState());

        // Naver 사용자 정보를 사용하기 위한 AccessToken 요청
        HttpEntity<NaverOAuthProperty> tokenReq = new HttpEntity<>(naverOAuthProperty);
        ResponseEntity<NaverTokenResponse> tokenRespEntity = restTemplate.exchange(tokenUrl, HttpMethod.POST, tokenReq, NaverTokenResponse.class);
        NaverTokenResponse tokens = tokenRespEntity.getBody();

        // 응답받은 AccessToken 을 사용해 사용자 정보 취득
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + tokens.getAccess_token());

        HttpEntity<MultiValueMap<String, String>> memberInfoReq = new HttpEntity<>(headers);
        ResponseEntity<NaverMemberInfoResponse> memberInfoRespEntity = restTemplate.exchange(
            naverOAuthProperty.getNaverMeUrl(), HttpMethod.POST, memberInfoReq, NaverMemberInfoResponse.class);
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

    public ResponseLogin authenticateWithKakao(OAuthCallbackParam param) {
        String tokenUrl = kakaoOAuthProperty.getKakaoTokenUrl(param.getCode());

        // Naver 사용자 정보를 사용하기 위한 AccessToken 요청
        ResponseEntity<KakaoTokenResponse> tokenRespEntity = restTemplate.exchange(tokenUrl, HttpMethod.POST, null, KakaoTokenResponse.class);
        KakaoTokenResponse tokens = tokenRespEntity.getBody();

        // 응답받은 AccessToken 을 사용해 사용자 정보 취득
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + tokens.getAccess_token());

        HttpEntity<MultiValueMap<String, String>> memberReq = new HttpEntity<>(headers);
        ResponseEntity<KakaoMemberResponse> memberResp = restTemplate.exchange(
            kakaoOAuthProperty.getMeUrl(), HttpMethod.POST, memberReq, KakaoMemberResponse.class);
        KakaoMemberResponse kakaoMember = memberResp.getBody();

        // 회원인지 확인
        String loginId = String.valueOf(kakaoMember.getId());
        String password = loginId + " ADMIN_TOKEN";
        Member memberByOauth = memberRepository.findByLoginId(loginId);

        // 등록된 회원이 아닐 경우 회원가입 진행
        if (memberByOauth == null) {
            KakaoAccount kakaoAccount = kakaoMember.getKakao_account();
            String encryptedPassword = bCryptPasswordEncoder.encode(password);
            String birthday = kakaoAccount.getBirthday();
            String birthFormat = String.format("%s-%s", birthday.substring(0,2), birthday.substring(2));

            Profile kakaoProfile = kakaoAccount.getProfile();
            memberByOauth = Member.create(loginId, kakaoProfile.getNickname(), kakaoAccount.getEmail(),
                password, birthFormat, kakaoProfile.getProfile_image_url(), encryptedPassword);

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
