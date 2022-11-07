package shop.kokodo.memberservice.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NaverTokenRequest {

    @Value("${oauth.naver.token-url}")
    private String tokenUrl;

    @Value("${oauth.naver.grant-type}")
    private String grantType;

    @Value("${oauth.naver.client-id}")
    private String clientId;

    @Value("${oauth.naver.client-secret}")
    private String clientSecret;


    public String getNaverTokenUrl(String code, String state) {
        return String.format("%s?grant_type=%s&client_id=%s&client_secret=%s&code=%s&state=%s",
            tokenUrl, grantType, clientId, clientSecret, code, state);
    }
}
