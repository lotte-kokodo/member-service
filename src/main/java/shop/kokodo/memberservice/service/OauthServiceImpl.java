package shop.kokodo.memberservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import shop.kokodo.memberservice.dto.NaverCallbackParam;
import shop.kokodo.memberservice.dto.NaverMemberInfoRequest;
import shop.kokodo.memberservice.dto.NaverMemberInfoResponse;
import shop.kokodo.memberservice.dto.NaverTokenRequest;
import shop.kokodo.memberservice.dto.NaverTokenResponse;
import shop.kokodo.memberservice.dto.response.Response;
import shop.kokodo.memberservice.oauth.PrincipalOauth2UserService;

@Service
@Slf4j
public class OauthServiceImpl implements OauthService {

    private final RestTemplate restTemplate;

    private final NaverTokenRequest naverTokenRequest;

    private final NaverMemberInfoRequest naverMemberInfoRequest;

    private final PrincipalOauth2UserService oauth2UserService;

    public OauthServiceImpl(RestTemplate restTemplate,
        NaverTokenRequest naverTokenRequest,
        NaverMemberInfoRequest naverMemberInfoRequest,
        PrincipalOauth2UserService oauth2UserService) {
        this.restTemplate = restTemplate;
        this.naverTokenRequest = naverTokenRequest;
        this.naverMemberInfoRequest = naverMemberInfoRequest;
        this.oauth2UserService = oauth2UserService;
    }

    public NaverMemberInfoResponse getMemberInfoFromNaver(NaverCallbackParam param) {
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
        NaverMemberInfoResponse naverMemberInfo = memberInfoRespEntity.getBody();

        return null;
    }

    @Override
    public Response authenticateWithNaver(NaverCallbackParam param) {

        return null;
    }

}
