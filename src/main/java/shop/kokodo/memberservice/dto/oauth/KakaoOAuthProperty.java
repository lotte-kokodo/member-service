package shop.kokodo.memberservice.dto.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoOAuthProperty {

    @Value("${oauth.kakao.token-url}")
    private String tokenUrl;

    @Value("${oauth.kakao.me-url}")
    private String meUrl;

    @Value("${oauth.kakao.grant-type}")
    private String grantType;

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.client-secret}")
    private String clientSecret;


    public String getKakaoTokenUrl(String code) {
        return String.format("%s?grant_type=%s&client_id=%s&client_secret=%s&code=%s",
            tokenUrl, grantType, clientId, clientSecret, code);
    }

    public String getMeUrl() {
        return meUrl;
    }
}
